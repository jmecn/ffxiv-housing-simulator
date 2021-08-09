package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import lombok.Getter;

import java.util.List;

public class XivRow implements IXivRow {

    // The IXivSheet the current row is in.
    @Getter
    private IXivSheet sheet;

    // the current row reads data from.
    @Getter
    private IRelationalRow sourceRow;

    public XivRow(IXivSheet sheet, IRelationalRow sourceRow) {
        this.sheet = sheet;
        this.sourceRow = sourceRow;
    }

    @Override
    public int getKey() {
        return sourceRow.getKey();
    }

    @Override
    public List<Object> getColumnValues() {
        return sourceRow.getColumnValues();
    }

    @Override
    public Object getDefaultValue() {
        return sourceRow.getDefaultValue();
    }

    @Override
    public Object get(int columnIndex) {
        return sourceRow.get(columnIndex);
    }

    @Override
    public Object get(String columnName) {
        return sourceRow.get(columnName);
    }

    @Override
    public Object getRaw(int columnIndex) {
        return sourceRow.getRaw(columnIndex);
    }

    @Override
    public Object getRaw(String columnName) {
        return sourceRow.getRaw(columnName);
    }

    public <T> T as(Class<T> clazz) {
        return null;
    }
}
