package ffxiv.housim.saintcoinach.db.xiv.sheets;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.XivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.ItemAction;

public class ItemActionSheet extends XivSheet<ItemAction> {
    public ItemActionSheet(XivCollection collection, IRelationalSheet<ItemAction> source) {
        super(collection, source, ItemAction.class);
    }
}
