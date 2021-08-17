package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import com.google.gson.JsonElement;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.math.Color;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class LinkCondition {
    public String keyColumnName;
    public int keyColumnIndex;
    public Object value;
    boolean valueTypeChanged;

    public boolean match(IDataRow row) {
        Object rowValue = row.get(keyColumnIndex);
        if (!valueTypeChanged && rowValue != null) {
            JsonElement e = (JsonElement) value;
            if (rowValue instanceof String) {
                value = e.getAsString();
            } else if (rowValue instanceof Boolean) {
                value = e.getAsBoolean();
            } else if (rowValue instanceof Byte) {
                value = e.getAsByte();
            } else if (rowValue instanceof Short) {
                value = e.getAsShort();
            } else if (rowValue instanceof Integer) {
                value = e.getAsInt();
            } else if (rowValue instanceof Long) {
                value = e.getAsLong();
            } else if (rowValue instanceof Float) {
                value = e.getAsFloat();
            } else if (rowValue instanceof Double) {
                value = e.getAsDouble();
            } else if (rowValue instanceof Color) {
                int val = e.getAsInt();
                value = new Color(val);
            } else {
                log.warn("Unknown row value type:{}, index:{}, name:{}", rowValue.getClass(), keyColumnIndex, keyColumnName);
            }
            valueTypeChanged = true;
        }
        return Objects.equals(rowValue, value);
    }
}