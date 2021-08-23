package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.ExCollection;
import ffxiv.housim.saintcoinach.db.ex.IRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;

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