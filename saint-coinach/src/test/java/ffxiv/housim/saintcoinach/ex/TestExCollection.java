package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.SqPack;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
        exCollection.getSheet("housingfurniture");
    }
}
