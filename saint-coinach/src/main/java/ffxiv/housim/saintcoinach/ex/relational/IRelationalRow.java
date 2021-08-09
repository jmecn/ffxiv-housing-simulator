package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IRow;

public interface IRelationalRow extends IRow {

    Object getDefaultValue();

    Object get(String columnName);

    Object getRaw(String columnName);
}
