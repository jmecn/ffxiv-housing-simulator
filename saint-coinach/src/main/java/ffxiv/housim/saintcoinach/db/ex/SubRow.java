package ffxiv.housim.saintcoinach.db.ex;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalDataSheet;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalColumn;
import lombok.Getter;

public class SubRow extends DataRowBase implements IRelationalDataRow {

    @Getter
    private IDataRow parentRow;

    public SubRow(IDataRow parent, int key, int offset) {
        super(parent.getSheet(), key, offset);
        parentRow = parent;
    }

    public String getFullKey() {
        return parentRow.getKey() + "." + key;
    }

    public IRelationalDataSheet<?> getSheet() {
        return (IRelationalDataSheet<?>) super.getSheet();
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

}
