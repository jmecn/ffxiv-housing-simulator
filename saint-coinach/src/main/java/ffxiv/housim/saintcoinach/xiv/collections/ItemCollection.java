package ffxiv.housim.saintcoinach.xiv.collections;

import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivCollection;
import ffxiv.housim.saintcoinach.xiv.entity.EventItem;
import ffxiv.housim.saintcoinach.xiv.entity.Item;
import ffxiv.housim.saintcoinach.xiv.entity.ItemBase;

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
