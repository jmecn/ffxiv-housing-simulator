package ffxiv.housim.saintcoinach;

import ffxiv.housim.saintcoinach.ex.Language;
import org.junit.Test;

import java.io.IOException;

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

        System.out.println(aRealmReversed);
    }
}
