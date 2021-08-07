package ffxiv.housim.saintcoinach.ex.row;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.sheet.IDataSheet;
import lombok.Getter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DataRowBase implements IDataRow {
    private Map<Integer, WeakReference<Object>> valueReferences;

    @Getter
    protected IDataSheet sheet;
    @Getter
    protected int key;
    @Getter
    protected int offset;

    protected DataRowBase(IDataSheet sheet, int key, int offset) {
        this.sheet = sheet;
        this.key = key;
        this.offset = offset;
        valueReferences = new ConcurrentHashMap<>();
    }

    @Override
    public Object getRow(int columnIndex) {
        WeakReference<Object> valueRef = valueReferences.get(columnIndex);
        if (valueRef != null && valueRef.get() != null) {
            return valueRef.get();
        }

        Column column = sheet.getHeader().getColumn(columnIndex);
        Object value = column.readRaw(sheet.getBuffer(), this);

        valueReferences.put(columnIndex, new WeakReference<>(value));

        return value;
    }

    public List<Object> getColumnValues() {
        byte[] buffer = sheet.getBuffer();
        Column[] columns = sheet.getHeader().getColumns();

        List<Object> list = new ArrayList<>(columns.length);

        for (Column column : columns) {
            list.add(column.read(buffer, this));
        }
        return list;
    }
}
