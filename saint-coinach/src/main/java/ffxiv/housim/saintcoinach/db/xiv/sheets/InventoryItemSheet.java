package ffxiv.housim.saintcoinach.db.xiv.sheets;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.XivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;

public class InventoryItemSheet extends XivSheet<Item> {
    public InventoryItemSheet(XivCollection collection, IRelationalSheet<Item> source) {
        super(collection, source, Item.class);
    }
}
