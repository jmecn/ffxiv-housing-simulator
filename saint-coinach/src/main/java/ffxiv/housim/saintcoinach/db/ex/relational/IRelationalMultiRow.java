package ffxiv.housim.saintcoinach.db.ex.relational;

import ffxiv.housim.saintcoinach.db.ex.IMultiRow;
import ffxiv.housim.saintcoinach.db.ex.Language;

public interface IRelationalMultiRow extends IRelationalRow, IMultiRow {

    IRelationalMultiSheet getSheet();

    Object get(String columnName, Language language);

    Object getRaw(String columnName, Language language);
}
