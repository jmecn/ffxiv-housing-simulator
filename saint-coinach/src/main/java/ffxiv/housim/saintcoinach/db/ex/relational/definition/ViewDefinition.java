package ffxiv.housim.saintcoinach.db.ex.relational.definition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ViewDefinition {

    @Getter
    private String sheetName;
    @Getter
    private List<ViewColumnDefinition> columnDefinitions = new ArrayList<>();

    public static ViewDefinition fromJson(JsonObject obj) {
        ViewDefinition def = new ViewDefinition();
        def.sheetName = obj.get("sheet").getAsString();

        JsonArray array = obj.getAsJsonArray("columns");
        for (JsonElement ele : array) {
            if (ele instanceof JsonObject) {
                def.columnDefinitions.add(ViewColumnDefinition.fromJson((JsonObject)ele));
            }
        }

        return def;
    }
}
