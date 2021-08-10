package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.util.Objects;

public class LinkCondition {
    public String keyColumnName;
    public int keyColumnIndex;
    public Object value;
    boolean valueTypeChanged;

    public boolean match(IDataRow row) {
        Object rowValue = row.get(keyColumnIndex);
        if (!valueTypeChanged && rowValue != null) {
            value = rowValue.getClass().cast(value);
            valueTypeChanged = true;
        }
        return Objects.equals(rowValue, value);
    }
}