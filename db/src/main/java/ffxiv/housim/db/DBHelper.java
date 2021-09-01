package ffxiv.housim.db;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.common.io.Resources;
import ffxiv.housim.db.entity.Version;
import ffxiv.housim.db.mapper.VersionMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * @title DBHelper
 * @author yanmaoyuan
 * @version 1.0
 */
@Slf4j
public class DBHelper {

    private final static String DB_FILE  = "data/cache.db";

    private final static String INIT_SQL = "sql/init.sql";

    // Testing if database was created
    private final static String TEST_SQL = "SELECT count(*) FROM `CACHE`";

    private static final HikariDataSource dataSource;

    private static final SqlSessionFactory sqlSessionFactory;

    static {
        log.info("Init dataSource");

        Path data = Paths.get("data");
        if (Files.notExists(data)) {
            try {
                Files.createDirectory(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Init HikariCP DataSource
        HikariConfig hikariCfg = new HikariConfig();
        hikariCfg.setJdbcUrl(org.sqlite.JDBC.PREFIX + DB_FILE);
        hikariCfg.setMaximumPoolSize(9);
        hikariCfg.setMinimumIdle(1);
        hikariCfg.setConnectionTimeout(30000);
        hikariCfg.setIdleTimeout(30000);

        dataSource = new HikariDataSource(hikariCfg);

        // Init MyBatis
        TransactionFactory factory = new JdbcTransactionFactory();
        Environment env = new Environment("dev", factory, dataSource);
        Configuration mybatisCfg = new Configuration(env);

        mybatisCfg.addMappers("ffxiv.housim.db.mapper");

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisCfg);
    }

    /**
     * Testing if database was initialized
     * 
     * @return
     */
    public static boolean initialized() {
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        int value = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(TEST_SQL);
            if (rs.next()) {
                value = rs.getInt(1);
            }
        } catch (SQLException e) {
            value = 0;
        } finally {
            close(stmt);
            close(rs);
            close(conn);
        }

        return value >= 1;
    }

    /**
     * Initialize db
     */
    public static void initDatabase() {
        log.info("Create database");
        URL in = Resources.getResource(INIT_SQL);
        try {
            String sql = Resources.asCharSource(in, StandardCharsets.UTF_8).read();
            executeUpdate(sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String sql) {
        Connection conn = getConnection();
        try (Statement state = conn.createStatement()) {
            state.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Failed execute:\n{}", sql, e);
        }
        close(conn);
    }

    /**
     * Get connection
     * @return
     */
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    /**
     * Close connection
     * 
     * @param conn
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    /**
     * Close statement
     * @param statement
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }
    }

    /**
     * Close resultSet
     * @param rs
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
    }

    /**
     * Close HikariCP DataSource
     */
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static void main(String[] args) {

        if (!DBHelper.initialized()) {
        	DBHelper.initDatabase();
        }

        try (SqlSession session = DBHelper.getSession()){
        	VersionMapper dao = session.getMapper(VersionMapper.class);
        	List<Version> list = dao.selectAll();
        	list.forEach(it -> {
        		System.out.println(it);
        	});
        	System.out.println(list.size());
        }

        close();
    }
}
