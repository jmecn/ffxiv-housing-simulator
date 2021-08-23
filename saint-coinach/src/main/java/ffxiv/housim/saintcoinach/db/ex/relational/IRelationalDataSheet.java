package ffxiv.housim.saintcoinach.db.ex.relational;

import ffxiv.housim.saintcoinach.db.ex.IDataSheet;

public interface IRelationalDataSheet<T extends IRelationalDataRow> extends IRelationalSheet<T>, IDataSheet<T> {
}
