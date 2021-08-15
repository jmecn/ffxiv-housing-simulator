package ffxiv.housim.saintcoinach.xiv.sheets;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.xiv.XivCollection;
import ffxiv.housim.saintcoinach.xiv.XivSheet;
import ffxiv.housim.saintcoinach.xiv.entity.ItemAction;

public class ItemActionSheet extends XivSheet<ItemAction> {
    public ItemActionSheet(XivCollection collection, IRelationalSheet<ItemAction> source) {
        super(collection, source, ItemAction.class);
    }
}
