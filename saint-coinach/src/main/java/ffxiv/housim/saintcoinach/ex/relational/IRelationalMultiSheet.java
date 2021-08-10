package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IMultiSheet;
import ffxiv.housim.saintcoinach.ex.Language;

public interface IRelationalMultiSheet<TMulti extends IRelationalMultiRow, TData extends IRelationalDataRow> extends IMultiSheet<TMulti, TData>, IRelationalSheet<TMulti> {

    IRelationalSheet<TData> getActiveSheet();

    TMulti get(int row);

    IRelationalSheet<TData> getLocalisedSheet(Language language);
}
