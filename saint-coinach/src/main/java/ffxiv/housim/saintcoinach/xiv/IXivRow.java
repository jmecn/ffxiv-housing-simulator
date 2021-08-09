package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;

public interface IXivRow extends IRelationalRow {

    IRelationalRow getSourceRow();

    IXivSheet getSheet();
}
