package ffxiv.housim.saintcoinach.xiv.entity.housing;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;
import ffxiv.housim.saintcoinach.xiv.entity.Item;

public class HousingInterior extends XivRow {
    public HousingInterior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public short getId() {
        return asInt16("Id");
    }

    public short getCategory() {
        return asInt16("Category");
    }
}
