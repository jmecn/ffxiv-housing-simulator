package ffxiv.housim.saintcoinach.db.ex;

import java.util.Collection;

public interface ISheet<T extends IRow> extends Iterable<T> {

    String getName();

    Header getHeader();

    ExCollection getCollection();

    int getCount();

    T get(int row);

    Object get(int row, int column);

    Collection<Integer> getKeys();

    boolean containsRow(int row);
}
