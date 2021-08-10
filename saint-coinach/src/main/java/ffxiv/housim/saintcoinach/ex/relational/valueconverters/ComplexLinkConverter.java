package ffxiv.housim.saintcoinach.ex.relational.valueconverters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.complexlink.SheetLinkData;
import ffxiv.housim.saintcoinach.ex.relational.definition.PositionedDataDefinition;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;

import java.lang.reflect.Type;
import java.util.Objects;

public class ComplexLinkConverter implements IValueConverter<Object> {

    private SheetLinkData[] links;

    @Override
    public String getTargetTypeName() {
        return "Row";
    }

    @Override
    public Type getTargetType() {
        return IRelationalRow.class;
    }

    @Override
    public Object convert(IDataRow row, Object rawValue) {
        int key = (int) rawValue;
        if (key == 0) {
            return null;
        }

        ExCollection collection = row.getSheet().getCollection();

        for (SheetLinkData link : links) {
            if (link.when != null && !link.when.match(row)) {
                continue;
            }

            IRow result = link.getRow(key, collection);
            if (result == null) {
                continue;
            }

            return link.projection.project(result);
        }

        return null;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "complexlink");

        JsonArray array = new JsonArray();
        for (SheetLinkData link : links) {
            array.add(link.toJson());
        }
        obj.add("links", array);

        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
        for (SheetLinkData link : links) {
            if (link.when != null) {
                PositionedDataDefinition keyDefinition = sheetDef.getDataDefinitions().stream()//
                        .filter(d -> Objects.equals(link.when.keyColumnName, d.innerDefinition.getName(0)))
                        .findFirst().orElse(null);

                if (keyDefinition == null) {
                    throw new IllegalArgumentException("Can't find conditional key column '{link.When.KeyColumnName}' in sheet '{sheetDef.Name}'");
                }

                link.when.keyColumnIndex = keyDefinition.getIndex();
            }
        }
    }

    public static ComplexLinkConverter fromJson(JsonObject obj) {
        JsonArray array = (JsonArray) obj.get("links");
        int size = array.size();
        SheetLinkData[] links = new SheetLinkData[size];
        for (int i = 0; i < size; i++) {
            JsonObject e = (JsonObject) array.get(i);
            links[i] = SheetLinkData.fromJson(e);
        }

        ComplexLinkConverter converter = new ComplexLinkConverter();
        converter.links = links;
        return converter;
    }

}
