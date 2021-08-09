package ffxiv.housim.saintcoinach.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.RelationalExCollection;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import lombok.Getter;

import java.lang.reflect.Type;

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
        int key = (int) rawValue;

        return sheet.get(key);
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
