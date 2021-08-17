package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.DataRowV2;
import ffxiv.housim.saintcoinach.ex.IDataSheet;

public class RelationalDataRowV2 extends DataRowV2 implements IRelationalDataRow {
    public RelationalDataRowV2(IDataSheet sheet, int key, int offset) {
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
        return getRaw(col.getIndex());
    }

    @Override
    public String toString() {
        RelationalColumn defCol = getSheet().getHeader().getDefaultColumn();
        return defCol == null
                ? String.format("%s#%d", getSheet().getHeader().getName(), getKey())
                : String.format("%s", getSubRow(defCol.getIndex()).getDefaultValue());
    }
}
