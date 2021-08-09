package ffxiv.housim.saintcoinach.ex;

public interface IDataRow extends IRow {

    int getOffset();

    @Override
    IDataSheet getSheet();
}
