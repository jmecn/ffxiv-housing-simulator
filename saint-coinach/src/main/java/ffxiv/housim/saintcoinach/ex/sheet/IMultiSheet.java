package ffxiv.housim.saintcoinach.ex.sheet;

import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.row.IMultiRow;
import ffxiv.housim.saintcoinach.ex.row.IRow;

public interface IMultiSheet<TMulti extends IMultiRow, TData extends IRow> extends ISheet<TMulti> {

    ISheet<TData> getActiveSheet();

    ISheet<TData> getLocalisedSheet(Language language);
}
