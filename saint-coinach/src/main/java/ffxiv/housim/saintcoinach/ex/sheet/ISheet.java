package ffxiv.housim.saintcoinach.ex.sheet;

import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.row.IRow;

import java.util.List;

public interface ISheet {
    String getName();
    Header getHeader();
    ExCollection getCollection();
    int getCount();
    IRow get(int row);
    Object get(int row, int column);
    List<Integer> getKeys();
    boolean containsRow(int row);
}
