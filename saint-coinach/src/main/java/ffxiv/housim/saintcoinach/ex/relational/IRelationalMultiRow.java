package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IMultiRow;
import ffxiv.housim.saintcoinach.ex.Language;

public interface IRelationalMultiRow extends IRelationalRow, IMultiRow {

    IRelationalMultiSheet getSheet();

    Object get(String columnName, Language language);

    Object getRaw(String columnName, Language language);
}
