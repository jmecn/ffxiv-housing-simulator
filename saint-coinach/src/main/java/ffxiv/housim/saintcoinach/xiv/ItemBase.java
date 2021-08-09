package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;

public abstract class ItemBase extends XivRow {

    public ItemBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
