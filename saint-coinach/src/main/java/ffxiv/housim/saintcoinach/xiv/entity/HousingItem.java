package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public abstract class HousingItem extends XivRow {
    public HousingItem(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getModelKey() {
        return asInt32("ModelKey");
    }

    public Item getItem() {
        return as(Item.class);
    }
}
