package ffxiv.housim.saintcoinach.db.xiv;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingFurniture;
import ffxiv.housim.saintcoinach.utils.ModelHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.TreeMap;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestXivEntities {
    private String gameDir;

    @Before
    public void getGameDir() {
        gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
    }

    @Test
    public void testGetSheetByClazz() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        coll.getSheetNameToTypeMap().forEach((k, v) -> {
            IXivSheet<?> sheet  = coll.getSheet(v);
            log.info("{}, {}", k, sheet.getCount());
            if ("Item".equals(k) || "Action".equals(k)) {
                return;
            }
            for (IXivRow item : sheet) {
                try {
                    item.getColumnValues();
                } catch (Exception e) {
                    log.error("{}, {}", k, item.getColumnValues(), e);
                }
            }
        });

    }

    @Test
    public void testENpcBase() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
            IXivSheet<ENpcBase> sheet  = coll.getSheet(ENpcBase.class);
            for (ENpcBase item : sheet) {
                try {
                    if (item.getBehavior() != null) {
                        Behavior behavior = item.getBehavior();

                        log.info("{}, {}, {}, {}", "ENpcBase", item.getColumnValues(), behavior, behavior.getColumnValues());
                    }
                } catch (Exception e) {
                    log.error("{}, {}", "ENpcBase", item.getColumnValues(), e);
                }
            }

    }

    @Test
    public void testBehavior() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<Behavior> sheet  = coll.getSheet(Behavior.class);
        Behavior behavior = sheet.get(30025);
        try {
            log.info("{}, {}", "Behavior", behavior.getColumnValues());
        } catch (Exception e) {
            log.error("{}, {}", "Behavior", behavior, e);
        }
        for (Behavior item : sheet) {
            try {
                log.info("{}, {}, {}, {}", "Behavior", item.getKey(), item.getFullKey(), item.getColumnValues());
            } catch (Exception e) {
                log.error("{}, {}", "Behavior", item.getColumnValues(), e);
            }
        }

    }
}
