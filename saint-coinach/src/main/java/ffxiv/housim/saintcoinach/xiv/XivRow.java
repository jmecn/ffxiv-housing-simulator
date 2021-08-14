package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.imaging.ImageFile;
import lombok.Getter;

import java.lang.reflect.Type;
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
        return as(type, type.getSimpleName());
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
        return null;
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
        return as(type, buildColumnName(column, indices));
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
        return asImage(buildColumnName(column, indices));
    }
}
