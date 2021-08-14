package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.IRow;

public interface IXivSubRow extends IXivRow {

    IRow getParentRow();

    int getParentKey();
}
