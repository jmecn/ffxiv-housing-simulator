package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;

import java.util.Arrays;

public class MultiSheetLinkData extends SheetLinkData {
    public String[] sheetNames;

    public MultiSheetLinkData(String[] sheetNames) {
        this.sheetNames = sheetNames;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = super.toJson();

        JsonArray array = new JsonArray();
        for (String sheetName : sheetNames) {
            array.add(sheetName);
        }
        obj.add("sheets", array);
        return obj;
    }

    @Override
    public IRow getRow(int key, ExCollection collection) {
        for (String sheetName : sheetNames) {
            IRelationalSheet sheet = (IRelationalSheet) collection.getSheet(sheetName);
            if (Arrays.stream(sheet.getHeader().getPages()).noneMatch(r -> r.contains(key))) {
                continue;
            }

            IRow row = rowProducer.getRow(sheet, key);
            if (row != null)
                return row;
        }
        return null;
    }
}