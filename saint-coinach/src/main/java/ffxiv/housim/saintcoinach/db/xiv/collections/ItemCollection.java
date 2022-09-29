package ffxiv.housim.saintcoinach.db.xiv.collections;

import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.EventItem;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.ItemBase;

import java.util.Iterator;

public class ItemCollection implements Iterable<ItemBase> {

    private XivCollection collection;
    private IXivSheet<Item> inventoryItemSheet;
    private IXivSheet<EventItem> eventItemSheet;

    public ItemCollection(XivCollection collection) {
        this.collection = collection;
        this.inventoryItemSheet = collection.getSheet(Item.class);
        this.eventItemSheet = collection.getSheet(EventItem.class);
    }

    @Override
    public Iterator<ItemBase> iterator() {
        return null;
    }
}
