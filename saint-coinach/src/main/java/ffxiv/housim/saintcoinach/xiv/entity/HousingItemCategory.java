package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public class HousingItemCategory extends XivRow {
    public HousingItemCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    @Override
    public String toString() {
        return getName();
    }
}
