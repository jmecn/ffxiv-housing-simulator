package ffxiv.housim.saintcoinach.db.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.enums.HousingItemCategory;
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
            HousingItemCategory hcat = HousingItemCategory.of(category.getHousingItemCategory());
            log.info("#{}:{}, 类别:{}->{}, 排序:{}", fur.getModelKey(), item.getName(), hcat.getName(), category.getCategory(), category.getOrder());
        });
    }
    @Test
    public void testHousingYardObject() {
        IXivSheet<HousingYardObject> sheet = aRealmReversed.getGameData().getSheet(HousingYardObject.class);
        Map<Integer, HousingYardObject> map = new HashMap<>();
        sheet.forEach(it -> {
            map.put(it.getItem().getKey(), it);
        });
        foreach(YardCatalogItemList.class, e-> {
            Item item = e.getItem();
            HousingYardObject obj = map.get(item.getKey());
            if (obj.getModelKey() == 0) {
                return;
            }
            var category = e.getCategory();
            HousingItemCategory hcat = HousingItemCategory.of(category.getHousingItemCategory());
            log.info("#{}:{}, 类别:{}->{}, 排序:{}", obj.getModelKey(), item.getName(), hcat.getName(), category.getCategory(), category.getOrder());
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
