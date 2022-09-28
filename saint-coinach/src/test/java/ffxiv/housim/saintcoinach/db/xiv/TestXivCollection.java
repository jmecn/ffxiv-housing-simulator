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
    public void testGetAction() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<Action> items = coll.getSheet(Action.class);
        for (Action row : items) {
            log.info("{}, {}, {}, {}, {}, {}, {}, {}, {}, {}", row.getKey(), row.getName(), row.getIcon(), row.getGainedStatus(), row.getCastTime(), row.getRecastTime(), row.getActionTransient(), row.getActionCategory(), row.getCostType(), row.getCost());
        }
    }

    @Test
    public void testGetStatus() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<Status> items = coll.getSheet(Status.class);
        for (Status row : items) {
            log.info("{}, {}, {}, {}, {}, {}", row.getKey(), row.getName(), row.getDescription(), row.canDispel(), row.getIcon(), row.getCategory());
        }
    }

    @Test
    public void testNpcEquip() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<NpcEquip> items = coll.getSheet(NpcEquip.class);
        for (NpcEquip row : items) {
            log.info("{}, {}, {}, {}, {}, {}", row.getKey(), row.getBodyModel(), row.getBodyDye(), row.isVisor(), row.getHandsModel(), row.getHandsDye());
        }
    }

    @Test
    public void testModelChara() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<ModelChara> items = coll.getSheet(ModelChara.class);
        for (ModelChara row : items) {
            log.info("{}, {}, {}, {}, {}", row.getKey(), row.getType(), row.getVariant(), row.getModelKey(), row.getBaseKey());
            try {
                log.info("{}", row.getType() == 3 ? ModelHelper.getMonsterModelDefinition(row) : null);
            } catch (Exception e) {
                log.error("getMonster model definition failed", e);
            }
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
