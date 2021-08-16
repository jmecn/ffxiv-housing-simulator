package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.relational.RelationalExCollection;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestExCollection {
    String gameDir;
    PackCollection collection;
    @Before
    public void getGameDir() {
        gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        collection = new PackCollection(gameDir + "/game/sqpack");
    }

    @Test
    public void testRoot() {
        ExCollection exCollection = new ExCollection(collection);
        exCollection.setActiveLanguage(Language.ChineseSimplified);
        ISheet sheet = exCollection.getSheet("BaseParam");

        log.info("name={}, rows={}, columns={}, class={}", sheet.getName(), sheet.getCount(), sheet.getHeader().getColumnCount(), sheet.getClass());

        int columnCount = sheet.getHeader().getColumnCount();

        Collection<Integer> keys = sheet.getKeys();
        for (Integer key : keys) {
            System.out.print(key + ": ");
            for (int i=0; i<columnCount; i++) {
                Object val = sheet.get(key, i);
                System.out.print(val + ",");
            }
            System.out.println();
        }
    }

    @Test
    public void testRelationalRow() {
        ARealmReversed aRealmReversed;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        RelationalExCollection coll = new RelationalExCollection(collection);
        coll.setDefinition(aRealmReversed.getGameData().getDefinition());

        coll.setActiveLanguage(Language.ChineseSimplified);
        ISheet sheet = coll.getSheet("HousingFurniture");

        log.info("name={}, rows={}, columns={}, class={}", sheet.getName(), sheet.getCount(), sheet.getHeader().getColumnCount(), sheet.getClass());

        int columnCount = sheet.getHeader().getColumnCount();

        Collection<Integer> keys = sheet.getKeys();
        for (Integer key : keys) {
            System.out.print(key + ": ");
            for (int i=0; i<columnCount; i++) {
                Object val = sheet.get(key, i);
                System.out.print(val + ",");
            }
            System.out.println();
        }
    }
}
