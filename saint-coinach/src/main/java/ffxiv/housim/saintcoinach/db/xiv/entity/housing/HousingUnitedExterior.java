package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.enums.HousingItemCategory;

public class HousingUnitedExterior extends XivRow {
    public HousingUnitedExterior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public HousingExterior getRof() {
        return getItem(0);
    }

    public HousingExterior getWal() {
        return getItem(1);
    }

    public HousingExterior getWid() {
        return getItem(2);
    }

    public HousingExterior getDor() {
        return getItem(3);
    }

    public HousingExterior getRf() {
        return getItem(4);
    }

    public HousingExterior getWl() {
        return getItem(5);
    }

    public HousingExterior getSg() {
        return getItem(6);
    }

    public HousingExterior getFnc() {
        return getItem(7);
    }

    public HousingExterior getItem(int i) {
        return as(HousingExterior.class, "Item[" + i + "]");
    }

    public HousingExterior getItem(HousingItemCategory cat) {
        return as(HousingExterior.class, "Item[" + (cat.getValue() - 1) + "]");
    }

}
