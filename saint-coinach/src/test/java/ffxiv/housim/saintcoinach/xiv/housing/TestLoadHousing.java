package ffxiv.housim.saintcoinach.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.xiv.IXivRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.xiv.entity.*;
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

    @Test(expected = IllegalArgumentException.class)
    public void testHousingLayoutLimit() {
        foreach(HousingLayoutLimit.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHousingItemCategory() {
        foreach(HousingItemCategory.class);
    }

    @Test
    public void testHousingPileLimit() {
        foreach(HousingPileLimit.class);
    }

    @Test
    public void testHouseRetainerPose() {
        foreach(HouseRetainerPose.class);
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
    public void testHousingYardObject() {
        foreach(HousingYardObject.class);
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


    public <T extends IXivRow> void foreach(Class<T> clazz) {
        IXivSheet<T> sheet = aRealmReversed.getGameData().getSheet(clazz);

        String name = sheet.getName();
        RelationalHeader header = sheet.getHeader();
        RelationalColumn[] columns = header.getColumns();
        String[] types = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            types[i] = columns[i].getValueType();
        }
        log.info("{}: {}", sheet.getName(), types);

        for (T t : sheet) {
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                values[i] = t.get(columns[i].getIndex());
            }

            if (t instanceof XivSubRow) {
                log.info("#{}: {}", ((XivSubRow)t).getFullKey(), values);
            } else if (t instanceof IXivRow) {
                log.info("#{}: {}", t.getKey(), values);
            }
        }

        log.info("row count: {}", sheet.getCount());
    }
}
