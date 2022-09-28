package ffxiv.housim.saintcoinach.db.xiv.orchestrion;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.db.xiv.music.orch.OrchestrionPath;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.sound.ScdFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestLoadOrchestrion {
    ARealmReversed aRealmReversed;

    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOrchestrionPath() {
        foreach(OrchestrionPath.class);
    }

    @Test
    public void testScd() {
        IXivSheet<OrchestrionPath> sheet = aRealmReversed.getGameData().getSheet(OrchestrionPath.class);
        for (OrchestrionPath row : sheet) {
            if (row.getFile() != null && StringUtils.isBlank(row.getFile())) {
                log.info("No file in OrchestrionPath#{}: {}", row.getKey(), row.getFile());
                continue;
            }
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(row.getFile().toLowerCase());
            if (file == null) {
                log.warn("No pack file found: OrchestrionPath#{}, file:{}", row.getKey(), row.getFile());
                continue;
            }
            ScdFile scd = new ScdFile(file);
            if (scd.getHeader().entryCount <= 0) {
                log.info("No entrys: OrchestrionPath#{}, file:{}", row.getKey(), row.getFile());
            } else {
                log.info("Loaded OrchestrionPath#{}, file:{}", row.getKey(), row.getFile());
            }
        }
    }

    @Test
    public void testFfxivScdFile() {
        PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile("music/ffxiv/bgm_con_bahamut_bigboss0.scd");
        assertNotNull(file);
        ScdFile scd = new ScdFile(file);
    }

    @Test
    public void testEx1ScdFile() {
        PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile("music/ex1/bgm_ex1_dungeon_xelphatol.scd");
        assertNotNull(file);
        ScdFile scd = new ScdFile(file);
    }

    public <T extends IXivRow> void foreach(Class<T> clazz) {
        IXivSheet<T> sheet = aRealmReversed.getGameData().getSheet(clazz);

        RelationalHeader header = sheet.getHeader();
        RelationalColumn[] columns = header.getColumns();
        String[] types = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            types[i] = columns[i].getValueType();
        }
        log.info("{}: {}", sheet.getName(), types);

        for (T xivRow : sheet) {
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                values[i] = xivRow.get(columns[i].getIndex());
            }

            if (xivRow instanceof XivSubRow) {
                XivSubRow xivSubRow = (XivSubRow) xivRow;
                log.info("#{}: {}", xivSubRow.getFullKey(), values);
            } else if (xivRow != null) {
                log.info("#{}: {}", xivRow.getKey(), values);
            }
        }

        log.info("row count: {}", sheet.getCount());
    }
}
