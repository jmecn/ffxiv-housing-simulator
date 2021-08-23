package ffxiv.housim.saintcoinach.db.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.math.XivQuad;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IValueConverter;

import java.lang.reflect.Type;

public class QuadConverter implements IValueConverter<XivQuad> {

    @Override
    public String getTargetTypeName() {
        return "XivQuad";
    }

    @Override
    public Type getTargetType() {
        return XivQuad.class;
    }

    @Override
    public XivQuad convert(IDataRow row, Object rawValue) {
        return new XivQuad((long) rawValue);
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
