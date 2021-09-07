package ffxiv.housim.saintcoinach.db.xiv.housing;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingItemCategory;
import ffxiv.housim.saintcoinach.io.*;
import ffxiv.housim.saintcoinach.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.sgb.ISgbData;
import ffxiv.housim.saintcoinach.scene.sgb.SgbFile;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class TestLoadHousingSgb {
    ARealmReversed aRealmReversed;

    Map<Integer, Item> i2i = new HashMap<>();
    Map<Integer, Item> e2i = new HashMap<>();
    Map<Integer, Item> ue2i = new HashMap<>();
    Map<Integer, Item> f2i = new HashMap<>();
    Map<Integer, Item> yo2i = new HashMap<>();

    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        try {
            aRealmReversed = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IXivSheet<Item> items = aRealmReversed.getGameData().getSheet(Item.class);
        for (Item i : items) {
            if (14 != i.getFilterGroup()) {// 14 for housing
                continue;
            }

            IXivRow row = i.getAdditionalData();
            if (row instanceof HousingInterior) {
                i2i.put(row.getKey(), i);
            } else if (row instanceof HousingExterior) {
                e2i.put(row.getKey(), i);
            } else if (row instanceof HousingUnitedExterior) {
                ue2i.put(row.getKey(), i);
            } else if (row instanceof HousingFurniture) {
                f2i.put(row.getKey(), i);
            } else if (row instanceof HousingYardObject) {
                yo2i.put(row.getKey(), i);
            }
        }
    }

    @Test
    public void testHousingExterior() {
        foreach(HousingExterior.class, e-> {
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(e.getModel());
            if (file != null) {
                SgbFile sgb = new SgbFile(file);
                log.info("#{}: {}, {}, {}, {}, {}", e.getKey(), e.getExteriorId(), e.getHousingItemCategory(), e.getPlaceName(), e.getHousingSize(), e.getModel());
            } else {
                log.info("#{}: {}, {}, {}, {}, {}, {}", e.getKey(), e.getExteriorId(), e.getHousingItemCategory(), e.getPlaceName(), e.getHousingSize(), e.getModel(), "NOT EXIST");
            }
        });
    }


    @Test
    public void testHousingUnitedExterior() {
        foreach(HousingUnitedExterior.class, e-> {
            HousingExterior rof = e.getItem(HousingItemCategory.ROF);
            HousingExterior wal = e.getItem(HousingItemCategory.WAL);
            HousingExterior wid = e.getItem(HousingItemCategory.WID);
            HousingExterior dor = e.getItem(HousingItemCategory.DOR);
            HousingExterior rf = e.getItem(HousingItemCategory.RF);
            HousingExterior wl = e.getItem(HousingItemCategory.WL);
            HousingExterior sg = e.getItem(HousingItemCategory.SG);
            HousingExterior fnc = e.getItem(HousingItemCategory.FNC);

            log.info("#{}:{} {}, {}, {}, {}, {}, {}, {}, {}", e.getKey(), ue2i.get(e.getKey()), rof, wal, wid, dor, rf, wl, sg, fnc);

        });
    }

    @Test
    public void testExterior() {
        PackCollection packs = aRealmReversed.getGameData().getPackCollection();

        Pack p01 = packs.tryGetPack(new PackIdentifier(1, 0, 0));
        Pack p02 = packs.tryGetPack(new PackIdentifier(2, 0, 0));

        travePack(p01);
        travePack(p02);

    }
    private void travePack(Pack pack) {
        System.out.println(pack);
        IndexSource is = (IndexSource) pack.getSource();
        Map<Integer, IndexDirectory> map = is.getIndex().getDirectories();

        map.forEach((dirKey, dir) -> {
            PackDirectory packDir = is.tryGetDirectory(dirKey);

            Map<Integer, IndexFile> map2 = dir.getFiles();
            map2.forEach((fileKey, indexFile) -> {
                PackFile file = packDir.tryGetFile(fileKey);
                if (file instanceof ModelFile mdl) {
                    MaterialDefinition mdf = mdl.getModelDefinition().getMaterials()[0];
                    if (mdf.getDefaultPath() != null) {
                        log.info("mtrl:{}", mdf.getDefaultPath());
                    }
                }
            });
        });
    }
    @Test
    public void testHousingInterior() {
        log.info("test housing interior");
        PackCollection packs = aRealmReversed.getGameData().getPackCollection();

        foreach(HousingInterior.class, e-> {
            Item item = i2i.get(e.getKey());

            HousingItemCategory hcat = HousingItemCategory.of(e.getHousingItemCategory());
            log.info("#{}: {}, {}, {}", e.getKey(), item, hcat.getName(), e.getOrder());

            switch (hcat) {
                case ROM_WL -> {
                    String mtrl = String.format("bgcommon/hou/dyna/mat/wl/%04d/material/rom_wl_2%04da.mtrl", e.getOrder(), e.getOrder());
                    PackFile file = packs.tryGetFile(mtrl);
                    log.info("material:{}, file:{}", mtrl, file);
                    break;
                }
                case ROM_FL -> {
                    String mtrl = String.format("bgcommon/hou/dyna/mat/fl/%04d/material/rom_fl_2%04da.mtrl", e.getOrder(), e.getOrder());
                    PackFile file = packs.tryGetFile(mtrl);
                    log.info("material:{}, file:{}", mtrl, file);
                    break;
                }
                case LMP -> {
                    String sgb = String.format("bgcommon/hou/dyna/lmp/lp/%04d/asset/lmp_s0_m%04d.sgb", e.getOrder(), e.getOrder());
                    PackFile file = packs.tryGetFile(sgb);
                    log.info("Scene:{}, file:{}", sgb, file);
                    break;
                }
            }
        });
    }

    @Test
    public void testHousingFurniture() {
        foreach(HousingFurniture.class, e-> {
            Item item = e.getItem();
            PackFile file = aRealmReversed.getGameData().getPackCollection().tryGetFile(e.getSgbPath());
            if (file != null) {
                SgbFile sgb = new SgbFile(file);
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
