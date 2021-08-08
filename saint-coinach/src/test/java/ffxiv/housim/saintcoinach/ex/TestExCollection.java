package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.SqPack;
import ffxiv.housim.saintcoinach.ex.sheet.IDataSheet;
import ffxiv.housim.saintcoinach.ex.sheet.ISheet;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestExCollection {
    PackCollection collection;
    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);

        SqPack sqPack = new SqPack(gameDir);
        String dataDir = sqPack.getPackDir();
        collection = new PackCollection(dataDir);
    }

    @Test
    public void testRoot() {
        ExCollection exCollection = new ExCollection(collection);
        exCollection.setActiveLanguage(Language.ChineseSimplified);
        ISheet sheet = exCollection.getSheet("Action");

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
