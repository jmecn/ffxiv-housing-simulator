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
import java.util.*;

import com.google.common.io.Resources;
import com.google.common.reflect.ClassPath;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Version;
import ffxiv.housim.db.mapper.VersionMapper;
import lombok.NonNull;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yanmaoyuan
 * @version 1.0
 */
public enum DBHelper {

    INSTANCE;

    Logger log = LoggerFactory.getLogger("DBHelper");

    private final static String DATA_DIR = "data";

    private final static String TEST_SQL = "select count(*) from preference where key = 'init'";

    private final Map<String, SqlSessionFactory> map;

    private final List<HikariDataSource> list;

    DBHelper() {
        Path data = Paths.get(DATA_DIR);
        if (Files.notExists(data)) {
            try {
                Files.createDirectory(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        map = new HashMap<>();
        list = new ArrayList<>(3);
    }

    /**
     * Testing if database was initialized
     * 
     * @return
     */
    public boolean initialized(@NonNull String db) {
        try (SqlSession session = getSession(db)) {
            Connection conn = session.getConnection();
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
            }

            return value >= 1;
        }
    }

    /**
     * Initialize db
     */
    public void initDatabase(String db) {
        log.info("Create database");
        String schema = "schema/" + db + ".sql";
        URL in = Resources.getResource(schema);
        try {
            String sql = Resources.asCharSource(in, StandardCharsets.UTF_8).read();
            executeUpdate(db, sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(String db, String sql) {
        try (SqlSession session = getSession(db)) {
            Connection conn = session.getConnection();
            try (Statement state = conn.createStatement()) {
                state.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("Failed execute:\n{}", sql, e);
            }
        }
    }

    public SqlSession getSession(@NonNull String db) {
        return getSqlSessionFactory(db, db).openSession(true);
    }

    public SqlSessionFactory getSqlSessionFactory(@NonNull String db, @NonNull String ds) {

        SqlSessionFactory sqlSessionFactory = map.get(db);
        if (sqlSessionFactory != null) {
            return sqlSessionFactory;
        }

        // Init HikariCP Schema
        HikariDataSource dataSource = getDataSource(ds);
        list.add(dataSource);

        // Init MyBatis
        TransactionFactory factory = new JdbcTransactionFactory();
        Environment env = new Environment(db, factory, dataSource);
        Configuration mybatisCfg = new Configuration(env);

        try {
            ClassPath cp =  ClassPath.from(DBHelper.class.getClassLoader());
            Set<ClassPath.ClassInfo> infoList = cp.getTopLevelClassesRecursive(DBHelper.class.getPackage().getName());
            for (ClassPath.ClassInfo info : infoList) {
                Class<?> clazz = info.load();
                Schema schema = clazz.getAnnotation(Schema.class);
                if (schema == null) {
                    continue;
                }
                for (String val : schema.value()) {
                    if (db.equals(val)) {
                        mybatisCfg.addMapper(clazz);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisCfg);
        map.put(db, sqlSessionFactory);
        return sqlSessionFactory;
    }

    // Init HikariCP Schema
    private HikariDataSource getDataSource(String name) {

        HikariConfig hikariCfg = new HikariConfig();
        hikariCfg.setJdbcUrl(org.sqlite.JDBC.PREFIX + DATA_DIR + "/" + name + ".db");
        hikariCfg.setMaximumPoolSize(9);
        hikariCfg.setMinimumIdle(1);
        hikariCfg.setConnectionTimeout(1000);
        hikariCfg.setIdleTimeout(300000);
        hikariCfg.setAutoCommit(true);

        return new HikariDataSource(hikariCfg);
    }

    /**
     * Close HikariCP Schema
     */
    public void close() {
        for (HikariDataSource ds : list) {
            if (ds != null) {
                ds.close();
            }
        }
    }

    public static void main(String[] args) {

        DBHelper db = DBHelper.INSTANCE;
        if (!db.initialized("cache")) {
        	db.initDatabase("cache");
        }

        try (SqlSession session = db.getSession("cache")){
        	VersionMapper dao = session.getMapper(VersionMapper.class);
        	List<Version> list = dao.selectAll();
        	list.forEach(System.out::println);
        	System.out.println(list.size());
        }

        db.close();
    }
}
