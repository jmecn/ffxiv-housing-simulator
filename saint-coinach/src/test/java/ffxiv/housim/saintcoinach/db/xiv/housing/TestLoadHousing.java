package ffxiv.housim.saintcoinach.db.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.PlaceName;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.XivMap;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestLoadHousing {
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
    public void testHouseRetainerPose() {
        foreach(HouseRetainerPose.class);
    }

    @Test
    public void testHousingPileLimit() {
        foreach(HousingPileLimit.class);
    }

    @Test
    public void testXivMap() {
        foreach(XivMap.class);
    }

    @Test
    public void testTerritoryType() {
        foreach(TerritoryType.class);
    }

    @Test
    public void testPlaceName() {
        foreach(PlaceName.class);
    }

    @Test
    public void testHousingAethernet() {
        foreach(HousingAethernet.class);
    }

    @Test
    public void testHousingExterior() {
        foreach(HousingExterior.class);
    }

    @Test
    public void testHousingUnitedExterior() {
        foreach(HousingUnitedExterior.class);
    }

    @Test
    public void testHousingInterior() {
        foreach(HousingInterior.class);
    }

    @Test
    public void testHousingFurniture() {
        foreach(HousingFurniture.class);
    }

    @Test
    public void testFurnitureCatalogCategory() {
        foreach(FurnitureCatalogCategory.class);
    }

    @Test
    public void testFurnitureCatalogItemList() {
        foreach(FurnitureCatalogItemList.class);
    }

    @Test
    public void testHousingYardObject() {
        foreach(HousingYardObject.class);
    }

    @Test
    public void testYardCatalogCategory() {
        foreach(YardCatalogCategory.class);
    }

    @Test
    public void testYardCatalogItemList() {
        foreach(YardCatalogItemList.class);
    }

    @Test
    public void testHousingTrainingDoll() {
        foreach(HousingTrainingDoll.class);
    }

    @Test
    public void testHousingMapMarkerInfo() {
        foreach(HousingMapMarkerInfo.class);
    }

    @Test
    public void testHousingLandSet() {
        foreach(HousingLandSet.class);
    }

    @Test
    public void testHousingPlacement() {
        foreach(HousingPlacement.class);
    }

    @Test
    public void testHousingPreset() {
        foreach(HousingPreset.class);
    }

    @Test
    public void testMap() {
        foreach(XivMap.class);
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
            if (xivRow.getKey() == 0) {
                continue;
            }
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
