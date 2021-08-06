package ffxiv.housim.saintcoinach.ex;

public interface IMultiSheet extends ISheet {

    ISheet getActiveSheet();
    IMultiRow get(int row);
    ISheet getLocalisedSheet(Language language);
}
