package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.ISheet;

public interface IRelationalSheet<T extends IRelationalRow> extends ISheet<T> {
    @Override
    RelationalHeader getHeader();

    @Override
    RelationalExCollection getCollection();

    Object get(int row, String columnName);

    IRelationalRow indexedLookup(String index, int key);
}
