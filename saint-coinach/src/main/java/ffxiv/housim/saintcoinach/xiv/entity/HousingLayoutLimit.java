package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public class HousingLayoutLimit extends XivRow {
    public HousingLayoutLimit(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getPersonalChamber() {
        return asInt32("PersonalChamber");
    }

    public int getCottage() {
        return asInt32("Small");
    }

    public int getHouse() {
        return asInt32("Medium");
    }

    public int getMansion() {
        return asInt32("Large");
    }
}
