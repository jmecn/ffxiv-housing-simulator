package ffxiv.housim.saintcoinach.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.RelationalExCollection;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;

import javax.management.relation.Relation;
import java.lang.reflect.Type;

public class GenericReferenceConverter implements IValueConverter<IRelationalRow> {
    @Override
    public String getTargetTypeName() {
        return "Row";
    }

    @Override
    public Type getTargetType() {
        return IRelationalRow.class;
    }

    @Override
    public IRelationalRow convert(IDataRow row, Object rawValue) {
        RelationalExCollection coll = (RelationalExCollection) row.getSheet().getCollection();
        int key = (int) rawValue;
        return coll.findReference(key);
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "generic");
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
    }

    public static GenericReferenceConverter fromJson(JsonObject obj) {
        return new GenericReferenceConverter();
    }
}
