package ffxiv.housim.saintcoinach.db.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingExterior;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingFurniture;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingInterior;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingYardObject;
import ffxiv.housim.saintcoinach.scene.sgb.SgbFile;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.xiv.entity.housing.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestLoadHousingSgb {
    ARealmReversed aRealmReversed;

    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHousingExterior() {
        foreach(HousingExterior.class, e-> {
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(e.getModel());
            if (file != null) {
                SgbFile sgb = new SgbFile(file);
                sgb.getData();
                log.info("#{}: {}, {}, {}, {}, {}", e.getKey(), e.getExteriorKey(), e.getExteriorType(), e.getPlaceName(), e.getHousingSize(), e.getModel());
            } else {
                log.info("#{}: {}, {}, {}, {}, {}, {}", e.getKey(), e.getExteriorKey(), e.getExteriorType(), e.getPlaceName(), e.getHousingSize(), e.getModel(), "NOT EXIST");
            }
        });
    }

    @Test
    public void testHousingInterior() {
        IXivSheet<Item> items = aRealmReversed.getGameData().getSheet(Item.class);

        foreach(HousingInterior.class, e-> {
            Item item = items.get(e.getKey());
            log.info("#{}: {}, {}, {}", e.getKey(), item, e.getId(), e.getCategory());
        });
    }

    @Test
    public void testHousingFurniture() {
        foreach(HousingFurniture.class, e-> {
            Item item = e.getItem();
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(e.getSgbPath());
            if (file != null) {
                SgbFile sgb = new SgbFile(file);
                sgb.getData();
                log.info("#{}: {}, {}, {}", e.getKey(), e.getModelKey(), item, e.getSgbPath());
            } else {
                log.info("#{}: {}, {}, {}, {}", e.getKey(), e.getModelKey(), item, e.getSgbPath(), "NOT EXIST");
            }
        });
    }

    @Test
    public void testHousingYardObject() {
        foreach(HousingYardObject.class, e-> {
            Item item = e.getItem();
            log.info("#{}: {}, {}, {}", e.getKey(), e.getModelKey(), item, e.getSgbPath());
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(e.getSgbPath());
            SgbFile sgb = new SgbFile(file);
            sgb.getData();
        });
    }

    public interface Visitor<T> {
        void visit(T e);
    }

    public <T extends IXivRow> void foreach(Class<T> clazz, Visitor<T> visitor) {
        IXivSheet<T> sheet = aRealmReversed.getGameData().getSheet(clazz);

        log.info("Visit sheet: {}, row count: {}", sheet.getName(), sheet.getCount());
        for (T xivRow : sheet) {
            visitor.visit(xivRow);
        }
    }
}
