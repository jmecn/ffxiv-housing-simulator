package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.DataRowV1;
import ffxiv.housim.saintcoinach.ex.IDataSheet;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RelationalDataRowV1 extends DataRowV1 implements IRelationalDataRow {

    private Map<String, WeakReference<Object>> valueReferences = new ConcurrentHashMap<>();

    public RelationalDataRowV1(IDataSheet sheet, int key, int offset) {
        super(sheet, key, offset);
    }

    @Override
    public IRelationalDataSheet getSheet() {
        return (IRelationalDataSheet) super.getSheet();
    }

    @Override
    public Object getDefaultValue() {
        RelationalColumn defCol = getSheet().getHeader().getDefaultColumn();
        return defCol == null ? null : get(defCol.getIndex());
    }

    @Override
    public Object get(String columnName) {
        WeakReference<Object> ref = valueReferences.get(columnName);
        if (ref != null && ref.get() != null) {
            return ref.get();
        }

        Object val = getColumnValue(columnName);

        valueReferences.put(columnName, new WeakReference<>(val));
        return val;
    }

    private Object getColumnValue(String columnName) {
        RelationalColumn col = getSheet().getHeader().findColumn(columnName);
        if (col == null) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }

        return get(col.getIndex());
    }

    @Override
    public Object getRaw(String columnName) {
        RelationalColumn col = getSheet().getHeader().findColumn(columnName);
        if (col == null) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return col.readRaw(getSheet().getBuffer(), this);
    }

    @Override
    public String toString() {
        RelationalColumn defCol = getSheet().getHeader().getDefaultColumn();
        return defCol == null
                ? String.format("%s#%d", getSheet().getHeader().getName(), getKey())
                : String.format("%s", get(defCol.getIndex()));
    }
}
