package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.SqPack;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.io.Pack;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.var;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestXivCollection {
    private String dataDir;

    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);

        SqPack sqPack = new SqPack(gameDir);
        dataDir = sqPack.getPackDir();
    }

    @Test
    public void testGetSheetByClazz() {
        XivCollection coll = new XivCollection(new PackCollection(dataDir));
        coll.setActiveLanguage(Language.ChineseSimplified);
        //IXivSheet<Item> items = coll.getSheet(Item.class);
        IRelationalSheet<?> sheet = coll.getSheet("Item");
        for (IRelationalRow row : sheet) {
            System.out.println(row.getKey());
        }
    }
}
