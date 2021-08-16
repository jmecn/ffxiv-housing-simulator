package ffxiv.housim.saintcoinach.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.math.Quad;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;

import java.lang.reflect.Type;

public class QuadConverter implements IValueConverter<Quad> {

    @Override
    public String getTargetTypeName() {
        return "Quad";
    }

    @Override
    public Type getTargetType() {
        return Quad.class;
    }

    @Override
    public Quad convert(IDataRow row, Object rawValue) {
        return new Quad((long) rawValue);
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "quad");
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
    }

    public static QuadConverter fromJson(JsonObject obj) {
        return new QuadConverter();
    }
}
