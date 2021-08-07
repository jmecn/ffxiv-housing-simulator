package ffxiv.housim.saintcoinach.ex.row;

import ffxiv.housim.saintcoinach.ex.sheet.IDataSheet;

public interface IDataRow extends IRow {
    int getOffset();
    IDataSheet getSheet();
}
