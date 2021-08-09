package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.IDataSheet;

import java.util.List;

public class RelationalDataIndex<T> implements IDataRow {
    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public IDataSheet getSheet() {
        return null;
    }

    @Override
    public int getKey() {
        return 0;
    }

    @Override
    public Object get(int columnIndex) {
        return null;
    }

    @Override
    public Object getRaw(int columnIndex) {
        return null;
    }

    @Override
    public List<Object> getColumnValues() {
        return null;
    }
}
