package ffxiv.housim.saintcoinach;

import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalMultiRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivSheet;
import ffxiv.housim.saintcoinach.xiv.entity.Item;
import ffxiv.housim.saintcoinach.xiv.entity.PlaceName;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertNotNull;

public class TestARealmReversed {

    @Test
    public void testConstructor() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IXivSheet<Item> sheet = aRealmReversed.getGameData().getSheet(Item.class);
        Iterator<Item> it = sheet.iterator();
        int i = 0;
        while(it.hasNext()) {
            Item e = it.next();
            System.out.printf("#%d: %s, %s\n%s\n", e.getKey(), e, e.get(10), e.get(8));
        }
    }
}
