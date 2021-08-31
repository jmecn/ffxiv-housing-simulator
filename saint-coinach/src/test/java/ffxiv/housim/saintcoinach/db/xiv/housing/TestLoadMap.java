package ffxiv.housim.saintcoinach.db.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.PlaceName;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.XivMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestLoadMap {
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
    public void testHousingPileLimit() {
        foreach(HousingPileLimit.class);
    }

    @Test
    public void testHouseRetainerPose() {
        foreach(HouseRetainerPose.class);
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
    public void testHousingMapMarkerInfo() {
        foreach(HousingMapMarkerInfo.class);
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
