package ffxiv.housim.saintcoinach.db.ex.relational;

import ffxiv.housim.saintcoinach.db.ex.IMultiSheet;
import ffxiv.housim.saintcoinach.db.ex.Language;

public interface IRelationalMultiSheet<TMulti extends IRelationalMultiRow, TData extends IRelationalDataRow>
        extends IMultiSheet<TMulti, TData>, IRelationalSheet<TMulti> {

    IRelationalSheet<TData> getActiveSheet();

    TMulti get(int row);

    IRelationalSheet<TData> getLocalisedSheet(Language language);
}
