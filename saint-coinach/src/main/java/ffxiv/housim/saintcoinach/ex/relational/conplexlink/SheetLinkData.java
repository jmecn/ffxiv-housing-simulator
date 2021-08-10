package ffxiv.housim.saintcoinach.ex.relational.conplexlink;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.IRow;

public abstract class SheetLinkData {
    public String projectedColumnName;
    public String keyColumnName;

    public IRowProducer rowProducer;
    public IProjectable projection;

    public LinkCondition when;

    public static SheetLinkData fromJson(JsonObject obj) {
        SheetLinkData data;
        if (obj.has("sheet")) {
            String sheetName = obj.get("sheet").getAsString();
            data = new SingleSheetLinkData(sheetName);
        } else if (obj.has("sheets")) {
            JsonArray array = (JsonArray) obj.get("sheets");
            int size = array.size();
            String[] sheetNames = new String[size];
            for (int i = 0; i < size; i++) {
                sheetNames[i] = array.get(i).getAsString();
            }
            data = new MultiSheetLinkData(sheetNames);
        } else {
            throw new IllegalArgumentException("complexlink link must contain either 'sheet' or 'sheets'.");
        }

        JsonElement proj = obj.get("project");
        if (proj == null)
            data.projection = new IdentityProjection();
        else {
            data.projectedColumnName = proj.getAsString();
            data.projection = new ColumnProjection(data.projectedColumnName);
        }

        JsonElement key = obj.get("key");
        if (key == null)
            data.rowProducer = new PrimaryKeyRowProducer();
        else {
            data.keyColumnName = key.getAsString();
            data.rowProducer = new IndexedRowProducer(data.keyColumnName);
        }

        JsonObject when = (JsonObject) obj.get("when");
        if (when != null) {
            LinkCondition condition = new LinkCondition();
            condition.keyColumnName = when.get("key").getAsString();
            condition.value = when.get("value");
            data.when = condition;
        }

        return data;
    }

    public abstract IRow getRow(int key, ExCollection collection);

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (projectedColumnName != null) {
            obj.addProperty("project", projectedColumnName);
        }
        if (keyColumnName != null) {
            obj.addProperty("key", keyColumnName);
        }
        if (when != null) {
            JsonObject whenObj = new JsonObject();
            whenObj.addProperty("key", when.keyColumnName);
            whenObj.addProperty("value", (int) when.value);
            obj.add("when", whenObj);
        }

        return obj;
    }
}