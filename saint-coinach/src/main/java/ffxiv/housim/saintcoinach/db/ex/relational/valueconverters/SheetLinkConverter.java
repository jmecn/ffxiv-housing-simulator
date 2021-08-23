package ffxiv.housim.saintcoinach.db.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalExCollection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

@Slf4j
public class SheetLinkConverter implements IValueConverter<IRelationalRow> {

    @Getter
    private String targetSheet;

    @Override
    public String getTargetTypeName() {
        return targetSheet;
    }

    @Override
    public Type getTargetType() {
        return IRelationalRow.class;
    }

    @Override
    public IRelationalRow convert(IDataRow row, Object rawValue) {
        RelationalExCollection coll = (RelationalExCollection) row.getSheet().getCollection();

        if (!coll.sheetExists(targetSheet)) {
            return null;
        }

        IRelationalSheet<?> sheet = coll.getSheet(targetSheet);
        if (rawValue instanceof Integer) {
            int key = (int) rawValue;

            return sheet.get(key);
        } else if (rawValue instanceof Short) {
            int key = (short) rawValue;

            return sheet.get(key);
        } else if (rawValue instanceof Boolean) {
            int key = (boolean) rawValue ? 1 : 0;

            return sheet.get(key);
        } else {
            log.warn("rawValue expected as int/short, actually {}", rawValue.getClass());
            return null;
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "link");
        obj.addProperty("target", targetSheet);
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {

    }

    public static SheetLinkConverter fromJson(JsonObject obj) {
        SheetLinkConverter converter = new SheetLinkConverter();
        converter.targetSheet = obj.get("target").getAsString();
        return converter;
    }
}
