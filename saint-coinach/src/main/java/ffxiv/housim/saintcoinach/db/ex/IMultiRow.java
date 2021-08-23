package ffxiv.housim.saintcoinach.db.ex;

public interface IMultiRow extends IRow {

    IMultiSheet getSheet();

    Object get(int columnIndex, Language language);

    Object getRaw(int columnIndex, Language language);
}
