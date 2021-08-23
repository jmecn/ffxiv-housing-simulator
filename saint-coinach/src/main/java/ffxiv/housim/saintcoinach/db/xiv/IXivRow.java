package ffxiv.housim.saintcoinach.db.xiv;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;

public interface IXivRow extends IRelationalRow {

    IRelationalRow getSourceRow();

    IXivSheet getSheet();
}
