package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.valueconverters.QuadConverter;
import lombok.Getter;

public class ViewColumnDefinition {

    @Getter
    private String columnName;
    @Getter
    private IValueConverter converter;

    public static ViewColumnDefinition fromJson(JsonObject obj) {
        JsonObject converterObj = obj.getAsJsonObject("converter");
        IValueConverter converter = null;
        if (converterObj != null) {
            String type = converterObj.get("type").getAsString();
            if ("quad".equals(type)) {
                converter = QuadConverter.fromJson(converterObj);
            } else {
                throw new IllegalArgumentException("Invalid converter type: " + type);
            }
        }

        ViewColumnDefinition def = new ViewColumnDefinition();
        def.columnName = obj.get("name").getAsString();
        def.converter = converter;
        return def;
    }
}
