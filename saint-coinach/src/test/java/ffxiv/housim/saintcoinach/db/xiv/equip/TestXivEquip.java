package ffxiv.housim.saintcoinach.db.xiv.equip;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.db.xiv.collections.EquipSlotCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HouseRetainerPose;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestXivEquip {
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
    public void testRace() {
        IXivSheet<Race> sheet = aRealmReversed.getGameData().getSheet(Race.class);

        foreach(Race.class);
    }

    @Test
    public void testTribe() {
        foreach(Tribe.class);
    }

    @Test
    public void testClassJob() {
        foreach(ClassJob.class);
    }

    @Test
    public void testEquipCollection() {
        EquipSlotCollection coll = aRealmReversed.getGameData().getEquipSlots();
        for (EquipSlot it : coll) {
            log.info("it:{}", it);
        }
    }

    @Test
    public void testItem() {
        foreach(Item.class);
    }

    @Test
    public void testEquipSlotCategory() {
        foreach(EquipSlotCategory.class);
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

            if (xivRow instanceof XivSubRow) {
                XivSubRow xivSubRow = (XivSubRow) xivRow;
                log.info("#{}: {}", xivSubRow.getFullKey(), values);
            } else if (xivRow != null) {
                log.info("#{}: {}", xivRow.getKey(), values);
            }
        }

        log.info("row count: {}", sheet.getCount());
    }
}
