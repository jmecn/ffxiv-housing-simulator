package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ex.ISheet;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.IMultiRow;
import ffxiv.housim.saintcoinach.ex.IRow;

public interface IMultiSheet<TMulti extends IMultiRow, TData extends IRow> extends ISheet<TMulti> {

    ISheet<TData> getActiveSheet();

    ISheet<TData> getLocalisedSheet(Language language);
}
