package ffxiv.housim.saintcoinach.db.ex;

public interface IMultiSheet<TMulti extends IMultiRow, TData extends IRow> extends ISheet<TMulti> {

    ISheet<TData> getActiveSheet();

    ISheet<TData> getLocalisedSheet(Language language);
}
