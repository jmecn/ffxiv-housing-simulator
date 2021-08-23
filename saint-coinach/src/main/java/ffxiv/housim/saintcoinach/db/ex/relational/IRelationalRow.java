package ffxiv.housim.saintcoinach.db.ex.relational;

import ffxiv.housim.saintcoinach.db.ex.IRow;

public interface IRelationalRow extends IRow {

    IRelationalSheet<?> getSheet();

    Object getDefaultValue();

    Object get(String columnName);

    Object getRaw(String columnName);
}
