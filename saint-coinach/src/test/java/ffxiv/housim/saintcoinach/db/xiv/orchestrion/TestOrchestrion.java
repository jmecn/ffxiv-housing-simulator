package ffxiv.housim.saintcoinach.db.xiv.orchestrion;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.Orchestrion;
import ffxiv.housim.saintcoinach.db.xiv.entity.OrchestrionCategory;
import ffxiv.housim.saintcoinach.db.xiv.entity.OrchestrionPath;
import ffxiv.housim.saintcoinach.db.xiv.entity.OrchestrionUiparam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestOrchestrion {
    private String gameDir;

    @Before
    public void getGameDir() {
        gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
    }

    @Test
    public void testOrchestrion() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<Orchestrion> items = coll.getSheet(Orchestrion.class);
        for (Orchestrion row : items) {
            log.info("{}, {}, {}, {}, {}", row.getKey(), row.getName(), row.getOrchestrionUiparam(), row.getOrchestrionPath(), row.getDescription());
        }
    }

    @Test
    public void testOrchestrionCategory() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<OrchestrionCategory> items = coll.getSheet(OrchestrionCategory.class);
        for (OrchestrionCategory row : items) {
            log.info("{}, {}, {}, {}, {}", row.getKey(), row.getName(), row.getHideOrder(), row.getIcon(), row.getOrder());
        }
    }


    @Test
    public void testOrchestrionPath() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<OrchestrionPath> items = coll.getSheet(OrchestrionPath.class);
        for (OrchestrionPath row : items) {
            log.info("{}, {}", row.getKey(), row.getFile());
        }
    }

    @Test
    public void testOrchestrionUiparam() {
        ARealmReversed aRealmReversed = null;
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XivCollection coll = aRealmReversed.getGameData();
        IXivSheet<OrchestrionUiparam> items = coll.getSheet(OrchestrionUiparam.class);
        for (OrchestrionUiparam row : items) {
            log.info("{}, {}, {}", row.getKey(), row.getOrchestrionCategory(), row.getOrder());
        }
    }


}
