package ffxiv.housim.saintcoinach.db.xiv;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.ClassJob;
import ffxiv.housim.saintcoinach.db.xiv.entity.ClassJobCategory;
import ffxiv.housim.saintcoinach.db.xiv.entity.Stain;
import ffxiv.housim.saintcoinach.db.xiv.entity.StainTransient;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingFurniture;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.TreeMap;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestXivCollection {
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
        IXivSheet<HousingFurniture> items = coll.getSheet(HousingFurniture.class);
        for (HousingFurniture row : items) {
            System.out.println(row.getItem());
        }
    }

    @Test
    public void testGetSheetByName() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IRelationalSheet<?> items = coll.getSheet("HousingFurniture");
        for (IRelationalRow row : items) {
            System.out.println(row.get("Item"));
        }
    }

    @Test
    public void testGetClassJob() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<ClassJob> items = coll.getSheet(ClassJob.class);
        for (ClassJob row : items) {
            log.info("{}, {}, {}, {}, {}, {}, {}, {}, {}, {}", row.getKey(), row.getName(), row.getAbbreviation(), row.getClassJobCategory(), row.getParentClassJob(), row.getStartingLevel(), row.getStartingWeapon(), row.getSoulCrystal(), row.getIcon(), row.getFramedIcon());
        }
    }

    @Test
    public void testStain() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<Stain> items = coll.getSheet(Stain.class);
        IXivSheet<StainTransient> transients = coll.getSheet(StainTransient.class);
        log.info("start iterator");
        TreeMap<Short, TreeMap<Short, Stain>> map = new TreeMap<>();
        for (Stain it : items) {
            if (it.getShade() == 0) {
                continue;
            }
            StainTransient tras = transients.get(it.getKey());
            log.info("{}, {}, {}, {}, {}, {}, {}, {}", it.getKey(), it.getColor(), it.getShade(), it.getSubOrder(), it.getName(), it.get4(), it.get5(), tras.getItem());

            TreeMap<Short, Stain> cache = map.get(it.getShade());
            if (cache == null) {
                cache = new TreeMap<>();
                map.put(it.getShade(), cache);
            }

            cache.put(it.getSubOrder(), it);
        }
        log.info("stain:{}", map);
    }

}
