package ffxiv.housim.saintcoinach.db.xiv;

import ffxiv.housim.saintcoinach.db.ex.IRow;

public interface IXivSubRow extends IXivRow {

    IRow getParentRow();

    int getParentKey();
}
