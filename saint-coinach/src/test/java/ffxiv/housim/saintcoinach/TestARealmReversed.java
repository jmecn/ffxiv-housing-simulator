package ffxiv.housim.saintcoinach;

import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertNotNull;

@Slf4j
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
            IXivRow row = e.getAdditionalData();
            if (row == null) {
                continue;
            }
            if (e.getFilterGroup() != 14 && e.getFilterGroup() != 15) {
                // 14 Housing 15 Stain
                continue;
            }

            log.info("#{}, {}, {}, {}, {}, {}", e.getKey(), e.getName(), e.getFilterGroup(), row.getClass().getSimpleName(), e.getAdditionalData(), e.getDescription());
        }
    }
}
