package ffxiv.housim.db;

import com.google.common.collect.Sets;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.IRow;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalHeader;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.Stain;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.*;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import lombok.extern.slf4j.Slf4j;

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

    public XivDatabase(ARealmReversed ffxiv) {
        this.ffxiv = ffxiv;
        this.gameVersion = ffxiv.getGameVersion();
    }

    public void init() {
        initTerritoryType();

        initItems();

        initLoadingImage();
    }

    public void initItems() {

        IXivSheet<Item> items = ffxiv.getGameData().getSheet(Item.class);

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

            if (row instanceof HousingInterior it) {
                interior2item.put(it.getKey(), e);
                item2interior.put(e.getKey(), it);
            } else if (row instanceof HousingExterior it) {
                exterior2item.put(it.getKey(), e);
                item2exterior.put(e.getKey(), it);
            } else if (row instanceof HousingPreset it) {
                preset2item.put(it.getKey(), e);
                item2preset.put(e.getKey(), it);
            } else if (row instanceof HousingUnitedExterior it) {
                unitedExterior2item.put(it.getKey(), e);
                item2unitedExterior.put(e.getKey(), it);
            } else if (row instanceof HousingFurniture it) {
                furniture2item.put(it.getKey(), e);
                item2furniture.put(e.getKey(), it);
            } else if (row instanceof HousingYardObject it) {
                yardObject2item.put(row.getKey(), e);
                item2yardObject.put(e.getKey(), it);
            } else if (row instanceof Stain it) {
                stain2item.put(it.getKey(), e);
                item2stain.put(e.getKey(), it);
            } else {
                log.info("what: {}, {}, {}", e.getFilterGroup(), row.getSheet().getName(), e.getName());
            }
        }

        IXivSheet<HousingExterior> exteriors = ffxiv.getGameData().getSheet(HousingExterior.class);

        for (HousingExterior e : exteriors) {
            if (e.getHousingItemCategory() == 0) {
                log.info("ignore HousingExterior #{}", e.getExteriorId());
                continue;
            }
            // TODO e
        }

        IXivSheet<HousingFurniture> furnitures = ffxiv.getGameData().getSheet(HousingFurniture.class);

        for (HousingFurniture f : furnitures) {
            if (f.getSgbPath() == null || f.getSgbPath().isBlank()) {
                log.info("ignore HousingFurniture #{}, {}", f.getModelKey(), f.getItem());
                continue;
            }
            if (f.getItem() == null || f.getItem().getName().isBlank()) {
                log.info("ignore HousingFurniture #{}, {}", f.getModelKey(), f.getSgbPath());
                continue;
            }
            // TODO f
        }

        IXivSheet<HousingYardObject> yardObjects = ffxiv.getGameData().getSheet(HousingYardObject.class);

        for (HousingYardObject f : yardObjects) {
            if (f.getSgbPath() == null || f.getSgbPath().isBlank()) {
                log.info("ignore HousingYardObject #{}, {}", f.getModelKey(), f.getItem());
                continue;
            }
            if (f.getItem() == null || f.getItem().getName().isBlank()) {
                log.info("ignore HousingYardObject #{}, {}", f.getModelKey(), f.getSgbPath());
                continue;
            }
            // TODO y
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
            var category = e.getCategory();
            HousingItemCategory hcat = HousingItemCategory.of(category.getHousingItemCategory());
            // log.info("#{}:{}, 类别:{}->{}, 排序:{}", fur.getModelKey(), item.getName(), hcat.getName(), category.getCategory(), category.getOrder());
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
            var category = e.getCategory();
            HousingItemCategory hcat = HousingItemCategory.of(category.getHousingItemCategory());
            // log.info("#{}:{}, 类别:{}->{}, 排序:{}", obj.getModelKey(), item.getName(), hcat.getName(), category.getCategory(), category.getOrder());
        }
    }

    public void initLoadingImage() {
        IRelationalSheet<?> sheet = ffxiv.getGameData().getSheet("LoadingImage");

        RelationalHeader header = sheet.getHeader();
        RelationalColumn[] columns = header.getColumns();
        String[] types = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            types[i] = columns[i].getValueType();
        }
        log.info("{}: {}", sheet.getName(), types);

        for (IRelationalRow xivRow : sheet) {
            if (xivRow.getKey() == 0) {
                continue;
            }
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                values[i] = xivRow.get(columns[i].getIndex());
            }

            if (xivRow instanceof XivSubRow xivSubRow) {
                log.info("#{}: {}", xivSubRow.getFullKey(), values);
            } else if (xivRow != null) {
                log.info("#{}: {}", xivRow.getKey(), values);
            }
        }

        log.info("row count: {}", sheet.getCount());
    }
    public void initTerritoryType() {
        IXivSheet<TerritoryType> sheet = ffxiv.getGameData().getSheet(TerritoryType.class);

        TreeMap<String, TerritoryType> map = new TreeMap<>();
        Pattern pattern = Pattern.compile("^[a-z]1i[1-4](_2)?$");
        for (TerritoryType territoryType : sheet) {
            if (territoryType.getKey() == 0) {
                continue;
            }
            if (territoryType.getBg() == null || territoryType.getBg().isBlank()) {
                continue;
            }
            String name = territoryType.getName();
            if (pattern.matcher(name).find()) {
                map.put(name, territoryType);
            }
        }

        map.values().forEach(territoryType -> {
            log.info("id:{}, name:{}, terr:{} > {} > {}, icon:{}, icon:{}", territoryType.getKey(), territoryType.getName(), territoryType.getRegionPlaceName(), territoryType.getPlaceName(), territoryType.getZonePlaceName(), territoryType.getPlaceNameIcon(), territoryType.getPlaceNameRegionIcon());
        });
    }

    public static void main(String[] args) throws Exception {
        String gameDir = System.getenv("FFXIV_HOME");
        ARealmReversed ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        XivDatabase db = new XivDatabase(ffxiv);
        db.init();
    }
}
