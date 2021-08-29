package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class HousingInterior extends XivRow {

    public HousingInterior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public short getOrder() {
        return asInt16("Order");
    }

    public short getHousingItemCategory() {
        return asInt16("HousingItemCategory");
    }
}
