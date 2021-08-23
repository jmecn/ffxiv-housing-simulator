package ffxiv.housim.saintcoinach.db.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.math.XivColor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

public class ColorConverer implements IValueConverter<XivColor> {

    @Getter
    @Setter
    private boolean includesAlpha;

    public ColorConverer() {
        includesAlpha = false;
    }

    @Override
    public String getTargetTypeName() {
        return "XivColor";
    }

    @Override
    public Type getTargetType() {
        return XivColor.class;
    }

    @Override
    public XivColor convert(IDataRow row, Object rawValue) {
        int value = (int) rawValue;
        return new XivColor(value, includesAlpha);
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "color");
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
    }

    public static ColorConverer fromJson(JsonObject obj) {
        return new ColorConverer();
    }
}
