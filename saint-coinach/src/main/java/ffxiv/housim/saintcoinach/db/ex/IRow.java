package ffxiv.housim.saintcoinach.db.ex;

import java.util.List;

public interface IRow {

    ISheet getSheet();

    int getKey();

    Object get(int columnIndex);

    Object getRaw(int columnIndex);

    List<Object> getColumnValues();
}
