package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;

public class YardCatalogItemList extends XivRow {
    public YardCatalogItemList(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public YardCatalogCategory getCategory() {
        return as(YardCatalogCategory.class, "Category");
    }

    public Item getItem() {
        return as(Item.class, "Item");
    }

    public Short getPatch() {
        return asInt16("Patch");
    }
}
