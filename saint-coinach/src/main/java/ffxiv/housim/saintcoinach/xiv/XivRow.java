package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.math.Quad;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.imaging.ImageFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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

    /**
     * Returns a string representation of the current row.
     * @return A string representation of the current row.
     */
    @Override
    public String toString() {
        return sourceRow.toString();
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

    /**
     * Build the full column name from a base and additional indices.
     *
     * @param column Base name of the column.
     * @param indices Indices for the full name.
     * @return The full column name built using <code>column</code> and <code>indices</code>.
     */
    public static String buildColumnName(String column, int ... indices) {
        if (indices == null || indices.length == 0) {
            return column;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(column);
        for (int i : indices) {
            sb.append('[').append(i).append(']');
        }
        return sb.toString();
    }

    /**
     * Get the value of the field in the column with the same name as a specific type.
     *
     * @param type The type that should be returned and also the name of the column.
     * @param <T> The type that should be returned and also the name of the column.
     * @return The value of the field in the column with the same name as the name of type <code>T</code>.
     */
    public <T> T as(Class<T> type) {
        XivSheetName attr = type.getAnnotation(XivSheetName.class);
        String columnName = attr != null ? attr.value() : type.getSimpleName();
        Object val = get(columnName);
        try {
            return type.cast(val);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * Get the value of the field in the column with the same base name as a specific type and given indices.
     *
     * @param type The type that should be returned and also the name of the column.
     * @param indices Indices for the full column.
     * @param <T> The type that should be returned and also the name of the column.
     * @return The value of the field in the column with the same base name as the name of type <code>T</code> and <code>indices</code>.
     */
    public <T> T as(Class<T> type, int ... indices) {
        XivSheetName attr = type.getAnnotation(XivSheetName.class);
        String columnName = attr != null ? attr.value() : type.getSimpleName();
        return (T) get(buildColumnName(columnName, indices));
    }

    /**
     * Get the value of a field in a specific column.
     *
     * @param type The type that should be returned.
     * @param column Name of the column from which to read.
     * @param <T> The type that should be returned.
     * @return The value of the field in <code>column</code>.
     */
    public <T> T as(Class<T> type, String column) {
        return (T) get(column);
    }

    /**
     * Get the value of a field in a specific column and indices.
     *
     * @param type The type that should be returned.
     * @param column Name of the column from which to read.
     * @param indices Indices for the full column.
     * @param <T> The type that should be returned.
     * @return The value of the field in <code>column</code> at <code>indices</code>.
     */
    public <T> T as(Class<T> type, String column, int ... indices) {
        return (T) get(buildColumnName(column, indices));
    }

    /**
     * Gets the value of a field from a specific column as an <see cref="ImageFile" />.
     *
     * @param column Name of the column from which to read.
     * @return The <see cref="ImageFile" /> in the <c>column</c> of the current row.
     */
    public ImageFile asImage(String column) {
        return (ImageFile) get(column);
    }

    /**
     * Gets the value of a field from a specific column and indices as an <see cref="ImageFile" />.
     *
     * @param column Name of the column from which to read.
     * @param indices Indices for the full column.
     * @return The <see cref="ImageFile" /> in the <c>column</c> at <c>indices</c> of the current row.
     */
    public ImageFile asImage(String column, int ... indices) {
        return (ImageFile) get(buildColumnName(column, indices));
    }

    public String asString(String column) {
        return (String) get(column);
    }

    public String asString(String column, int ... indices) {
        return (String) get(buildColumnName(column, indices));
    }

    public Boolean asBoolean(String column) {
        return (boolean) get(column);
    }

    public Boolean asBoolean(String column, int ... indices) {
        return (boolean) get(buildColumnName(column, indices));
    }

    public byte asByte(String column) {
        return (byte) get(column);
    }

    public byte asByte(String column, int ... indices) {
        return (byte) get(buildColumnName(column, indices));
    }

    public short asInt16(String column) {
        return (short) get(column);
    }

    public short asInt16(String column, int ... indices) {
        return (short) get(buildColumnName(column, indices));
    }

    public int asInt32(String column) {
        return (int) get(column);
    }

    public int asInt32(String column, int ... indices) {
        return (int) get(buildColumnName(column, indices));
    }

    public long asInt64(String column) {
        return (long) get(column);
    }

    public long asInt64(String column, int ... indices) {
        return (long) get(buildColumnName(column, indices));
    }

    public float asSingle(String column) {
        return (float) get(column);
    }

    public float asSingle(String column, int ... indices) {
        return (float) get(buildColumnName(column, indices));
    }

    public double asDouble(String column) {
        return (double) get(column);
    }

    public double asDouble(String column, int ... indices) {
        return (double) get(buildColumnName(column, indices));
    }

    public Quad asQuad(String column) {
        return (Quad) get(column);
    }

    public Quad asQuad(String column, int ... indices) {
        return (Quad) get(buildColumnName(column, indices));
    }

    public int[] asIntArray(String column) {
        int a = (int) get(column);
        int[] b = new int[4];
        b[3] = (a >> 24 & 0xFF);
        b[2] = (a >> 16 & 0xFF);
        b[1] = (a >> 8  & 0xFF);
        b[0] = (a       & 0xFF);

        return b;
    }
}
