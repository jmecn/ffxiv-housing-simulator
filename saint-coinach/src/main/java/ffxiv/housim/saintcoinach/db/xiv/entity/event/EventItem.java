package ffxiv.housim.saintcoinach.db.xiv.entity.event;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.ItemBase;

public class EventItem extends ItemBase {

    public EventItem(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
