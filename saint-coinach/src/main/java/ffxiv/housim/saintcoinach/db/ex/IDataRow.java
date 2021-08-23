package ffxiv.housim.saintcoinach.db.ex;

public interface IDataRow extends IRow {

    int getOffset();

    @Override
    IDataSheet getSheet();
}
