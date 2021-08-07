package ffxiv.housim.saintcoinach.ex.row;

import ffxiv.housim.saintcoinach.ex.sheet.ISheet;

import java.util.List;

public interface IRow {
    ISheet getSheet();
    int getKey();
    Object get(int columnIndex);
    Object getRow(int columnIndex);
    List<Object> getColumnValues();
}
