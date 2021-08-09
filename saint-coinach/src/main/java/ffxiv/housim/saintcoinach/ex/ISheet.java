package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.IRow;

import java.util.Collection;

public interface ISheet<R extends IRow> {

    String getName();

    Header getHeader();

    ExCollection getCollection();

    int getCount();

    R get(int row);

    Object get(int row, int column);

    Collection<Integer> getKeys();

    boolean containsRow(int row);
}
