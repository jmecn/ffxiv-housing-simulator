package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

// TODO need implements
public class Item extends ItemBase {

    public Item(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public boolean isUnique() {
        return asBoolean("IsUnique");
    }

    public boolean isUntradable() {
        return asBoolean("IsUntradable");
    }

    public boolean isDyeable() {
        return asBoolean("IsDyeable");
    }

    public boolean isIndisposable() {
        return asBoolean("IsIndisposable");
    }

    public boolean isCollectable() {
        return asBoolean("IsCollectable");
    }
}
