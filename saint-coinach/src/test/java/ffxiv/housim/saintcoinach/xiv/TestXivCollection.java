package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.xiv.entity.HousingFurniture;
import ffxiv.housim.saintcoinach.xiv.entity.Stain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

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
    public void testStain() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<Stain> items = coll.getSheet(Stain.class);
        log.info("start iterator");
        for (Stain it : items) {
            log.info("{}, {}, {}, {}, {}, {}, {}", it.getKey(), it.getColor(), it.getShade(), it.getSubOrder(), it.getName(), it.get4(), it.get5());
        }
        System.out.println(items.getCount());
    }

}
