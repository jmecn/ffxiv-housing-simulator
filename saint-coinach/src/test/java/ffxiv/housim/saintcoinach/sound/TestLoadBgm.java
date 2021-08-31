package ffxiv.housim.saintcoinach.sound;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.bgm.BGM;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestLoadBgm {
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
    public void testBGM() {
        foreach(BGM.class);
    }

    @Test
    public void testScd() {
        IXivSheet<BGM> sheet = aRealmReversed.getGameData().getSheet(BGM.class);
        for (BGM bgm : sheet) {
            if (bgm.getFile() != null && bgm.getFile().isBlank()) {
                log.info("No file in BGM#{}: {}", bgm.getKey(), bgm.getFile());
                continue;
            }
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(bgm.getFile().toLowerCase());
            if (file == null) {
                log.warn("No pack file found: BGM#{}, file:{}", bgm.getKey(), bgm.getFile());
                continue;
            }
            ScdFile scd = new ScdFile(file);
            if (scd.getHeader().entryCount <= 0) {
                log.info("No entrys: BGM#{}, file:{}", bgm.getKey(), bgm.getFile());
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

            if (xivRow instanceof XivSubRow xivSubRow) {
                log.info("#{}: {}", xivSubRow.getFullKey(), values);
            } else if (xivRow != null) {
                log.info("#{}: {}", xivRow.getKey(), values);
            }
        }

        log.info("row count: {}", sheet.getCount());
    }
}
