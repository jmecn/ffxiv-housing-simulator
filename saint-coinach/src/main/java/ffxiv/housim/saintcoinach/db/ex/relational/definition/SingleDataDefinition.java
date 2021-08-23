package ffxiv.housim.saintcoinach.db.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.db.ex.relational.valueconverters.*;

import java.lang.reflect.Type;

public class SingleDataDefinition implements IDataDefinition {

    private String name;
    private IValueConverter converter;

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public Object convert(IDataRow row, Object value, int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return converter != null ? converter.convert(row, value) : value;
    }

    @Override
    public String getName(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return name;
    }

    @Override
    public String getValueTypeName(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return converter != null ? converter.getTargetTypeName() : null;
    }

    @Override
    public Type getValueType(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return converter != null ? converter.getTargetType() : null;
    }

    @Override
    public IDataDefinition clone() {
        SingleDataDefinition def = new SingleDataDefinition();
        def.name = name;
        def.converter = converter;
        return def;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        if (converter != null) {
            obj.add("converter", converter.toJson());
        }
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
        if (converter != null) {
            converter.resolveReferences(sheetDef);
        }
    }

    public static SingleDataDefinition fromJson(JsonObject obj) {
        IValueConverter<?> converter = null;
        JsonObject converterObj = (JsonObject) obj.get("converter");
        if ( converterObj != null) {
            String type = converterObj.get("type").getAsString();
            if ("color".equals(type)) {
                converter = ColorConverer.fromJson(converterObj);
            } else if ("generic".equals(type)) {
                converter = GenericReferenceConverter.fromJson(converterObj);
            } else if ("icon".equals(type)) {
                converter = IconConverter.fromJson(converterObj);
            } else if ("multiref".equals(type)) {
                converter = MultiReferenceConverter.fromJson(converterObj);
            } else if ("link".equals(type)) {
                converter = SheetLinkConverter.fromJson(converterObj);
            } else if ("tomestone".equals(type)) {
                converter = TomestoneOrItemReferenceConverter.fromJson(converterObj);
            } else if ("complexlink".equals(type)) {
                converter = ComplexLinkConverter.fromJson(converterObj);
            } else {
                throw new IllegalArgumentException("Invalid converter type.");
            }
        }

        SingleDataDefinition def = new SingleDataDefinition();
        def.name = obj.get("name").getAsString();
        def.converter = converter;
        return def;
    }
}
