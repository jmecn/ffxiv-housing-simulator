package ffxiv.housim.db;

import ffxiv.housim.db.entity.*;
import ffxiv.housim.db.mapper.*;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.Stain;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import ffxiv.housim.saintcoinach.math.XivColor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.*;
import java.util.regex.Pattern;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/4
 */
@Slf4j
public class XivDatabase {
    private final ARealmReversed ffxiv;

    private final String gameVersion;

    private DBHelper db = DBHelper.INSTANCE;

    public final static int UNKNOWN_CATALOG = 999;

    public final static String CACHE = "cache";

    public final static String FFXIV = "ffxiv";

    Map<Integer, Item> interior2item = new HashMap<>();
    Map<Integer, Item> exterior2item = new HashMap<>();
    Map<Integer, Item> unitedExterior2item = new HashMap<>();
    Map<Integer, Item> preset2item = new HashMap<>();
    Map<Integer, Item> furniture2item = new HashMap<>();
    Map<Integer, Item> yardObject2item = new HashMap<>();
    Map<Integer, Item> stain2item = new HashMap<>();

    Map<Integer, HousingInterior> item2interior = new HashMap<>();
    Map<Integer, HousingExterior> item2exterior = new HashMap<>();
    Map<Integer, HousingUnitedExterior> item2unitedExterior = new HashMap<>();
    Map<Integer, HousingPreset> item2preset = new HashMap<>();
    Map<Integer, HousingFurniture> item2furniture = new HashMap<>();
    Map<Integer, HousingYardObject> item2yardObject = new HashMap<>();
    Map<Integer, Stain> item2stain = new HashMap<>();

    Map<Integer, FurnitureCatalogCategory> furniture2catalog = new HashMap<>();
    Map<Integer, YardCatalogCategory> yardObject2catalog = new HashMap<>();

    List<FurnitureCatalog> furnitureCatalogList = new ArrayList<>();

    public XivDatabase(ARealmReversed ffxiv) {
        this.ffxiv = ffxiv;
        this.gameVersion = ffxiv.getGameVersion();

        db.getSqlSessionFactory(CACHE, CACHE);
        if (!db.initialized(CACHE)) {
            db.initDatabase(CACHE);
        }

        db.getSqlSessionFactory(FFXIV, gameVersion);
        if (!db.initialized(FFXIV)) {
            db.initDatabase(FFXIV);
        }
    }

    private boolean isInitialized(String key) {
        try (SqlSession session = db.getSession(FFXIV)) {
            String init = session.getMapper(PreferenceMapper.class).get(key);
            return ("true".equals(init));
        }
    }

    private void setInitialized(String key) {
        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(PreferenceMapper.class).put(key, "true");
        }
    }

    public void init() {
        if (isInitialized("init_all")) {
            return;
        }

        initTerrain();
        initIndoor();
        // initOutdoor();
    }

    private void initIndoor() {
        if (isInitialized("init_indoor")) {
            return;
        }
        initItems();
        initStain();
        initFurnitureCategory();
        initInterior();
        initFurniture();

        setInitialized("init_indoor");
    }

    private void initOutdoor() {
        if (isInitialized("init_outdoor")) {
            return;
        }

        initYardObjectCategory();
        initUnitedExterior();
        initPreset();
        initExterior();
        initYardObject();

        //setInitialized("init_outdoor");
    }

    public void initItems() {

        IXivSheet<Item> items = ffxiv.getGameData().getSheet(Item.class);
        log.info("scan items..{}", items.getCount());

        for (Item e : items) {
            if (e.getFilterGroup() == 0) {
                continue;
            }
            if (e.getFilterGroup() != 14 && e.getFilterGroup() != 15) {
                // 14 Housing 家具
                // 15 Stain 染色剂
                // 18 TreasureHuntRank 藏宝图
                // 20 GardeningSeed 种子
                // 25 AetherialWheel 以太转轮
                // 26 CompanyAction 部队特效
                // 27 TripleTriadCard 九宫幻卡
                // 28 AirshipExplorationPart 飞空艇
                // 36 SubmarinePart 潜水艇
                continue;
            }

            IXivRow row = e.getAdditionalData();
            if (row == null) {
                continue;
            }

            if (row instanceof HousingInterior) {
                HousingInterior it = (HousingInterior) row;
                interior2item.put(it.getKey(), e);
                item2interior.put(e.getKey(), it);
            } else if (row instanceof HousingExterior) {
                HousingExterior it = (HousingExterior) row;
                exterior2item.put(it.getKey(), e);
                item2exterior.put(e.getKey(), it);
            } else if (row instanceof HousingPreset) {
                HousingPreset it = (HousingPreset) row;
                preset2item.put(it.getKey(), e);
                item2preset.put(e.getKey(), it);
            } else if (row instanceof HousingUnitedExterior) {
                HousingUnitedExterior it = (HousingUnitedExterior) row;
                unitedExterior2item.put(it.getKey(), e);
                item2unitedExterior.put(e.getKey(), it);
            } else if (row instanceof HousingFurniture) {
                HousingFurniture it = (HousingFurniture) row;
                furniture2item.put(it.getKey(), e);
                item2furniture.put(e.getKey(), it);
            } else if (row instanceof HousingYardObject) {
                HousingYardObject it = (HousingYardObject) row;
                yardObject2item.put(row.getKey(), e);
                item2yardObject.put(e.getKey(), it);
            } else if (row instanceof Stain) {
                Stain it = (Stain) row;
                stain2item.put(it.getKey(), e);
                item2stain.put(e.getKey(), it);
            } else {
                log.info("what: {}, {}, {}", e.getFilterGroup(), row.getSheet().getName(), e.getName());
            }
        }
    }

    /**
     * Init furniture catalog
     */
    public void initFurnitureCategory() {

        if (isInitialized("init_furniture_catalog"))  {
            return;
        }

        IXivSheet<FurnitureCatalogCategory> list = ffxiv.getGameData().getSheet(FurnitureCatalogCategory.class);
        for (FurnitureCatalogCategory e : list) {
            FurnitureCatalog entity = new FurnitureCatalog();
            entity.setId(e.getKey());
            entity.setCategory((int) e.getHousingItemCategory());
            entity.setName(e.getCategory());
            entity.setOrder((int) e.getOrder());
            furnitureCatalogList.add(entity);
        }

        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(FurnitureCatalogMapper.class).saveAll(furnitureCatalogList);
            log.info("init furniture_catalog, count:{}", furnitureCatalogList.size());
        }

        setInitialized("init_furniture_catalog");
    }

    public void initFurniture() {
        if (isInitialized("init_furniture")) {
            return;
        }

        IXivSheet<FurnitureCatalogItemList> furnitureCatalogItemLists = ffxiv.getGameData().getSheet(FurnitureCatalogItemList.class);
        for (FurnitureCatalogItemList e : furnitureCatalogItemLists) {
            if (e.getPatch() == 0) {
                continue;
            }
            Item item = e.getItem();
            if (item == null || item.getKey() == 0) {
                continue;
            }
            HousingFurniture fur = item2furniture.get(item.getKey());
            if (fur.getModelKey() == 0) {
                continue;
            }
            furniture2catalog.put(item.getKey(), e.getCategory());
        }

        IXivSheet<HousingFurniture> furnitures = ffxiv.getGameData().getSheet(HousingFurniture.class);

        List<Furniture> result = new ArrayList<>(furnitures.getCount());
        for (HousingFurniture f : furnitures) {
            if (StringUtils.isBlank(f.getSgbPath())) {
                continue;
            }

            Item item = furniture2item.get(f.getKey());

            if (item == null || StringUtils.isBlank(item.getName())) {
                continue;
            }

            FurnitureCatalogCategory cat = furniture2catalog.get(item.getKey());

            // TODO f
            Furniture entity = new Furniture();
            entity.setId(f.getKey());
            entity.setItemId(item.getKey());
            entity.setName(item.getName());
            entity.setIsDyeable(item.isDyeable() ? 1 : 0);
            entity.setModel(f.getSgbPath());
            entity.setIcon(item.getIcon().getPath());

            entity.setCategory(f.getHousingItemCategory());
            if (cat == null) {
                log.warn("not catalog found. #{}: {}, category:{}", f.getKey(), item, f.getHousingItemCategory());
                entity.setCatalog(UNKNOWN_CATALOG);
            } else {
                entity.setCatalog(cat.getKey());
            }
            result.add(entity);
        }

        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(FurnitureMapper.class).saveAll(result);
            log.info("init furniture, count={}", result.size());
        }

        setInitialized("init_furniture");
    }

    public void initInterior() {
        if (isInitialized("init_interior")) {
            return;
        }

        IXivSheet<HousingInterior> interiors = ffxiv.getGameData().getSheet(HousingInterior.class);

        List<Interior> result = new ArrayList<>(interiors.getCount());
        for (HousingInterior e : interiors) {
            Item item = interior2item.get(e.getKey());
            if (item == null || StringUtils.isBlank(item.getName())) {
                continue;
            }

            int category = e.getHousingItemCategory();
            int key = e.getOrder();
            String path;
            if (category == HousingItemCategory.ROM_FL.getValue()) {
                path = String.format("bgcommon/hou/dyna/mat/fl/%04d/material/rom_fl_2%04da.mtrl", key, key);
            } else if (category == HousingItemCategory.ROM_WL.getValue()) {
                path = String.format("bgcommon/hou/dyna/mat/wl/%04d/material/rom_wl_2%04da.mtrl", key, key);
            } else if (category == HousingItemCategory.LMP.getValue()) {
                path = String.format("bgcommon/hou/dyna/lmp/lp/%04d/asset/lmp_s0_m%04d.sgb", key, key);
            } else {
                log.warn("Unknown interior category", e.getKey(), category);
                continue;
            }

            Interior entity = new Interior();
            entity.setId(e.getKey());
            entity.setName(item.getName());
            entity.setItemId(item.getKey());
            entity.setCategory(category);
            entity.setOrder(e.getOrder());
            entity.setPath(path);
            entity.setIcon(item.getIcon().getPath());

            result.add(entity);
        }

        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(InteriorMapper.class).saveAll(result);
            log.info("init interior, count={}", result.size());
        }

        setInitialized("init_interior");
    }

    private void initYardObjectCategory() {

        if (isInitialized("init_yard_catalog"))  {
            return;
        }

        List<YardCatalog> result = new ArrayList<>();
        IXivSheet<YardCatalogCategory> list = ffxiv.getGameData().getSheet(YardCatalogCategory.class);
        for (YardCatalogCategory e : list) {
            YardCatalog entity = new YardCatalog();
            entity.setId(e.getKey());
            entity.setCategory(e.getHousingItemCategory());
            entity.setName(e.getCategory());
            entity.setOrder(e.getOrder());
            result.add(entity);
        }

        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(YardCatalogMapper.class).saveAll(result);
            log.info("init yard_catalog, count:{}", result.size());
        }

        setInitialized("init_yard_catalog");
    }

    private void initUnitedExterior() {
        IXivSheet<HousingUnitedExterior> sheet = ffxiv.getGameData().getSheet(HousingUnitedExterior.class);
        for (HousingUnitedExterior e : sheet) {
            Item item = unitedExterior2item.get(e.getKey());
            if (item == null || StringUtils.isBlank(item.getName())) {
                continue;
            }

            HousingExterior rof = e.getItem(HousingItemCategory.ROF);
            HousingExterior wal = e.getItem(HousingItemCategory.WAL);
            HousingExterior wid = e.getItem(HousingItemCategory.WID);
            HousingExterior dor = e.getItem(HousingItemCategory.DOR);
            HousingExterior rf = e.getItem(HousingItemCategory.RF);
            HousingExterior wl = e.getItem(HousingItemCategory.WL);
            HousingExterior sg = e.getItem(HousingItemCategory.SG);
            HousingExterior fnc = e.getItem(HousingItemCategory.FNC);

            log.info("#{}:{} item:{}, icon:{}, rof:{}, wal:{}, wid:{}, dor:{}, rf:{}, wl:{}, sg:{}, fnc:{}", e.getKey(), item.getName(), item.getKey(), item.getIcon().getPath(),
                    rof, wal, wid, dor, rf, wl, sg, fnc);
        }
    }
    private void initPreset() {
        IXivSheet<HousingPreset> sheet = ffxiv.getGameData().getSheet(HousingPreset.class);
        for (HousingPreset e : sheet) {
            Item item = preset2item.get(e.getKey());
            if (item == null || StringUtils.isBlank(item.getName())) {
                continue;
            }

            log.info("#{}:{} item:{}, icon:{}", e.getKey(), item.getName(), item.getKey(), item.getIcon().getPath());
        }
    }
    public void initExterior() {

        IXivSheet<HousingExterior> sheet = ffxiv.getGameData().getSheet(HousingExterior.class);
        List<Exterior> result = new ArrayList<>(sheet.getCount());
        for (HousingExterior e : sheet) {
            if (e.getHousingItemCategory() == 0) {
                log.info("ignore HousingExterior #{}", e.getExteriorId());
                continue;
            }

            Item it  = exterior2item.get(e.getKey());
            log.info("item:{}, exterior:{}, model:{}", it, e, e.getModel());
            // TODO e
        }
    }

    public void initYardObject() {
        if (isInitialized("init_yard_object")) {
            return;
        }

        IXivSheet<YardCatalogItemList> yardCatalogItemLists = ffxiv.getGameData().getSheet(YardCatalogItemList.class);
        for (YardCatalogItemList e : yardCatalogItemLists) {
            if (e.getPatch() == 0) {
                continue;
            }
            Item item = e.getItem();
            if (item == null || item.getKey() == 0) {
                continue;
            }
            HousingYardObject obj = item2yardObject.get(item.getKey());
            if (obj.getModelKey() == 0) {
                continue;
            }
            yardObject2catalog.put(item.getKey(), e.getCategory());
        }

        IXivSheet<HousingYardObject> yardObjects = ffxiv.getGameData().getSheet(HousingYardObject.class);
        List<YardObject> result = new ArrayList<>(yardObjects.getCount());
        for (HousingYardObject f : yardObjects) {
            if (StringUtils.isBlank(f.getSgbPath())) {
                log.info("ignore HousingYardObject #{}, {}", f.getModelKey(), f.getItem());
                continue;
            }
            if (f.getItem() == null || StringUtils.isBlank(f.getItem().getName())) {
                log.info("ignore HousingYardObject #{}, {}", f.getModelKey(), f.getSgbPath());
                continue;
            }
            Item item = f.getItem();
            YardCatalogCategory cat = yardObject2catalog.get(item.getKey());

            // TODO f
            YardObject entity = new YardObject();
            entity.setId(f.getKey());
            entity.setItemId(item.getKey());
            entity.setName(item.getName());
            entity.setIsDyeable(item.isDyeable() ? 1 : 0);
            entity.setModel(f.getSgbPath());
            entity.setIcon(item.getIcon().getPath());

            entity.setCategory(f.getHousingItemCategory());
            if (cat == null) {
                log.warn("not catalog found. #{}: {}, category:{}", f.getKey(), item, f.getHousingItemCategory());
                entity.setCatalog(UNKNOWN_CATALOG);
            } else {
                entity.setCatalog(cat.getKey());
            }
            result.add(entity);
        }

        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(YardObjectMapper.class).saveAll(result);
            log.info("init yard_object, count={}", result.size());
        }

        setInitialized("init_yard_object");
    }

    /**
     * init terrains
     */
    private void initTerrain() {
        if (isInitialized("init_terr")) {
            return;
        }

        IXivSheet<TerritoryType> sheet = ffxiv.getGameData().getSheet(TerritoryType.class);

        TreeMap<String, TerritoryType> map = new TreeMap<>();
        Pattern pattern = Pattern.compile("^[a-z]1i[1-4]$");
        for (TerritoryType territoryType : sheet) {
            if (territoryType.getKey() == 0) {
                continue;
            }
            if (StringUtils.isBlank(territoryType.getBg())) {
                continue;
            }
            String name = territoryType.getName();
            if (pattern.matcher(name).find()) {
                map.put(name, territoryType);
            }
        }

        List<Terr> result = new ArrayList<>(map.size());
        map.values().forEach(e -> {
            Terr entity = new Terr();
            entity.setId(e.getKey());
            entity.setName(e.getName());
            entity.setPlaceName(e.getPlaceName().getName());
            entity.setModel(e.getBg());
            result.add(entity);
        });

        try (SqlSession session = db.getSession(FFXIV)){
            session.getMapper(TerrMapper.class).saveAll(result);
            log.info("init terrain, count={}", result.size());
        }

        setInitialized("init_terr");
    }

    /**
     * init dye colors
     */
    private void initStain() {
        if (isInitialized("init_stain")) {
            return;
        }

        IXivSheet<Stain> stains = ffxiv.getGameData().getSheet(Stain.class);
        List<Dye> result = new ArrayList<>(stains.getCount());
        for (Stain e : stains) {
            Item item = stain2item.get(e.getKey());
            if (item == null) {
                continue;
            }

            Dye entity = new Dye();
            entity.setId(e.getKey());
            entity.setItemId(item.getKey());
            entity.setName(item.getName());
            entity.setShade(e.getShade());
            entity.setOrder(e.getSubOrder());

            XivColor color = e.getColor();
            entity.setRed(color.r & 0xFF);
            entity.setGreen(color.g & 0xFF);
            entity.setBlue(color.b & 0xFF);

            result.add(entity);
        }


        try (SqlSession session = db.getSession(FFXIV)) {
            session.getMapper(DyeMapper.class).saveAll(result);
            log.info("init dye, count={}", result.size());
        }

        setInitialized("init_stain");
    }

    public static void main(String[] args) throws Exception {
        String gameDir = System.getenv("FFXIV_HOME");
        ARealmReversed ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        XivDatabase db = new XivDatabase(ffxiv);
        db.init();
    }
}
