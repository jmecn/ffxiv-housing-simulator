package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.ISheet;

public interface IRelationalSheet<T extends IRelationalRow> extends ISheet<T> {

    RelationalHeader getHeader();

    RelationalExCollection getCollection();

    T get(int row);

    Object get(int row, String columnName);

    IRelationalRow indexedLookup(String index, int key);
}
