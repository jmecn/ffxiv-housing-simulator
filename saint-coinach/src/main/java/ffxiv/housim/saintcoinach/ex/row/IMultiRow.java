package ffxiv.housim.saintcoinach.ex.row;

import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.sheet.IMultiSheet;

public interface IMultiRow extends IRow {

    IMultiSheet getSheet();

    Object get(int columnIndex, Language language);

    Object getRaw(int columnIndex, Language language);
}
