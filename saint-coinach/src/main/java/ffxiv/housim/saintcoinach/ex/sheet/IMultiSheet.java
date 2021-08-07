package ffxiv.housim.saintcoinach.ex.sheet;

import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.row.IMultiRow;

public interface IMultiSheet extends ISheet {

    ISheet getActiveSheet();
    IMultiRow get(int row);
    ISheet getLocalisedSheet(Language language);
}
