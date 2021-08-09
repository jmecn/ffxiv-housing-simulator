package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonObject;

public class DataDefinitionSerializer {

    public static IDataDefinition fromJson(JsonObject obj) {
        String type = obj.get("type").getAsString();
        if (type == null) {
            return SingleDataDefinition.fromJson(obj);
        } else if (type.equals("group")) {
            return GroupDataDefinition.fromJson(obj);
        } else if (type.equals("repeat")) {
            return RepeatDataDefinition.fromJson(obj);
        } else {
            throw new IllegalArgumentException("Invalid definition type.");
        }
    }
}
