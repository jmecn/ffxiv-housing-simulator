package ffxiv.housim.saintcoinach.xiv.entity.housing;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;
import ffxiv.housim.saintcoinach.xiv.entity.Item;

public abstract class HousingItem extends XivRow {
    public HousingItem(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getModelKey() {
        return asInt32("ModelKey");
    }

    public Item getItem() {
        return as(Item.class, "Item");
    }
}
