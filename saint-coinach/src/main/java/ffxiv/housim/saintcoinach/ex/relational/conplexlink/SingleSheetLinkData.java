package ffxiv.housim.saintcoinach.ex.relational.conplexlink;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;

public class SingleSheetLinkData extends SheetLinkData {
    public String sheetName;

    public SingleSheetLinkData(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = super.toJson();
        obj.addProperty("sheet", sheetName);
        return obj;
    }

    @Override
    public IRow getRow(int key, ExCollection collection) {
        IRelationalSheet sheet = (IRelationalSheet) collection.getSheet(sheetName);
        return rowProducer.getRow(sheet, key);
    }
}