package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.xiv.entity.HousingFurniture;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class TestXivCollection {
    private String gameDir;

    @Before
    public void getGameDir() {
        gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
    }

    @Test
    public void testGetSheetByClazz() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        coll.setActiveLanguage(Language.ChineseSimplified);
        IXivSheet<HousingFurniture> items = coll.getSheet(HousingFurniture.class);
        for (HousingFurniture row : items) {
            System.out.println(row.getItem());
        }
    }
}
