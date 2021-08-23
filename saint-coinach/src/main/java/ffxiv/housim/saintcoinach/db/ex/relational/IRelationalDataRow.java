package ffxiv.housim.saintcoinach.db.ex.relational;

import ffxiv.housim.saintcoinach.db.ex.IDataRow;

public interface IRelationalDataRow extends IRelationalRow, IDataRow {

    IRelationalDataSheet<?> getSheet();
}