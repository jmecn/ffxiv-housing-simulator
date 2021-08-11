package ffxiv.housim.saintcoinach;

import com.google.gson.Gson;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalMultiRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalMultiSheet;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.xiv.Item;
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

        IRelationalSheet<?> sheet = aRealmReversed.getGameData().getSheet("Item");
        Iterator<?> it = sheet.iterator();
        int i = 0;
        while(it.hasNext()) {
            IRelationalMultiRow e = (IRelationalMultiRow) it.next();
            System.out.printf("#%d: %s, %s\n%s\n", e.getKey(), e, e.get(10), e.get(8));
        }
    }
}
