package ffxiv.housim.saintcoinach.db.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.ExCollection;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.IRow;
import ffxiv.housim.saintcoinach.db.ex.ISheet;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.db.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TomestoneOrItemReferenceConverter implements IValueConverter<IRelationalRow> {

    static Map<Integer, Item> tomestoneKeyByRewardIndex;

    @Override
    public String getTargetTypeName() {
        return "Item";
    }

    @Override
    public Type getTargetType() {
        return IRelationalRow.class;
    }

    @Override
    public IRelationalRow convert(IDataRow row, Object rawValue) {
        if (tomestoneKeyByRewardIndex == null) {
            tomestoneKeyByRewardIndex = buildTomestoneRewardIndex(row.getSheet().getCollection());
        }

        int key = (int) rawValue;


        Item item = tomestoneKeyByRewardIndex.get(key);
        if (item != null) {
            return item;
        }

        IRelationalSheet<?> items = (IRelationalSheet<?>)row.getSheet().getCollection().getSheet("Item");
        return items.get(key);
    }

    private Map<Integer, Item> buildTomestoneRewardIndex(ExCollection coll) {
        Map<Integer, Item> index = new HashMap<>();

        ISheet<?> sheet = coll.getSheet("TomestonesItem");

        for (IRow r : sheet) {
            XivRow row = (XivRow) r;
            int rewardIndex = (int)row.getRaw(2);
            if (rewardIndex > 0) {
                index.put(rewardIndex, row.as(Item.class));
            }
        }

        return index;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "tomestone");
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {

    }

    public static TomestoneOrItemReferenceConverter fromJson(JsonObject obj) {
        return new TomestoneOrItemReferenceConverter();
    }
}
