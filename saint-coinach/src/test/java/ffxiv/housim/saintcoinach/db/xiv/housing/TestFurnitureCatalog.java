package ffxiv.housim.saintcoinach.db.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.scene.sgb.SgbFile;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestFurnitureCatalog {
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
    public void testFurnitureCatalog() {
        IXivSheet<HousingFurniture> sheet = aRealmReversed.getGameData().getSheet(HousingFurniture.class);

        Map<Integer, HousingFurniture> map = new HashMap<>();
        sheet.forEach(it -> {
            map.put(it.getItem().getKey(), it);
        });
        foreach(FurnitureCatalogItemList.class, e-> {
            if (e.getPatch() == 0) {
                return;
            }
            Item item = e.getItem();
            var category = e.getCategory();
            HousingFurniture fur = map.get(item.getKey());
            log.info("key:{}, name:{}, category:{}, cat:{}, order:{}, sgb：{}", fur.getModelKey(), item.getName(), category.getHousingItemCategory(), category.getCategory(), category.getOrder(), fur.getSgbPath());
        });
    }
    @Test
    public void testHousingYardObject() {
        IXivSheet<HousingYardObject> sheet = aRealmReversed.getGameData().getSheet(HousingYardObject.class);
        foreach(YardCatalogItemList.class, e-> {
            Item item = e.getItem();
            var category = e.getCategory();

            sheet.get(item.getKey());

            log.info("name:{}, category:{}", item.getName(), category);
//            log.info("#{}: {}, {}, {}", e.getKey(), e.getModelKey(), item, e.getSgbPath());
//            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(e.getSgbPath());
//            SgbFile sgb = new SgbFile(file);
        });
    }

    public interface Visitor<T> {
        void visit(T e);
    }

    public <T extends IXivRow> void foreach(Class<T> clazz, TestLoadHousingSgb.Visitor<T> visitor) {
        IXivSheet<T> sheet = aRealmReversed.getGameData().getSheet(clazz);
        log.info("Visit sheet: {}, row count: {}", sheet.getName(), sheet.getCount());
        for (T xivRow : sheet) {
            visitor.visit(xivRow);
        }
    }
}
