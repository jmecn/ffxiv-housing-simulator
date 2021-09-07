package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;

public abstract class HousingItem extends XivRow {
    public HousingItem(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getHousingItemCategory() {
        return asInt16("HousingItemCategory");
    }

    public int getModelKey() {
        return asInt16("ModelKey");
    }

    public Item getItem() {
        return as(Item.class, "Item");
    }
}
