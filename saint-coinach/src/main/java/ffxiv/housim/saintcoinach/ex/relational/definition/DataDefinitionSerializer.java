package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataDefinitionSerializer {

    public static IDataDefinition fromJson(JsonObject obj) {
        JsonElement ele = obj.get("type");
        String type = ele != null ? ele.getAsString() : null;
        if (type == null) {
            return SingleDataDefinition.fromJson(obj);
        } else if ("group".equals(type)) {
            return GroupDataDefinition.fromJson(obj);
        } else if ("repeat".equals(type)) {
            return RepeatDataDefinition.fromJson(obj);
        } else {
            log.error("Invalid definition type:{}", type);
            throw new IllegalArgumentException("Invalid definition type.");
        }
    }
}