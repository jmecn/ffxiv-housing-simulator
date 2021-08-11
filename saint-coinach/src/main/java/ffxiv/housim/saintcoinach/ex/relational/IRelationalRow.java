package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IRow;

public interface IRelationalRow extends IRow {

    IRelationalSheet<?> getSheet();

    Object getDefaultValue();

    Object get(String columnName);

    Object getRaw(String columnName);
}
