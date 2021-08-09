package ffxiv.housim.saintcoinach.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.lang.reflect.Type;

public class ColorConverer implements IValueConverter<Color> {

    @Getter
    @Setter
    private boolean includesAlpha;

    public ColorConverer() {
        includesAlpha = false;
    }

    @Override
    public String getTargetTypeName() {
        return "Color";
    }

    @Override
    public Type getTargetType() {
        return Color.class;
    }

    @Override
    public Color convert(IDataRow row, Object rawValue) {
        int value = (int) rawValue;
        return new Color(value, includesAlpha);
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
