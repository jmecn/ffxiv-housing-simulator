package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IDataSheet;

public interface IRelationalDataSheet<T extends IRelationalDataRow> extends IRelationalSheet<T>, IDataSheet<T> {
}
