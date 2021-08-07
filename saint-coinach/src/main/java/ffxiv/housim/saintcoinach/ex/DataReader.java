package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ex.row.IDataRow;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class used in reading data from EX data files.
 */
public abstract class DataReader {
    /**
     * Mappings of type identifiers used in EX headers to their corresponding {@link DataReader}.
     */
    private static Map<Integer, DataReader> DataReaders;

    /**
     * Gets the name of the value type read.
     *
     * @return The name of the value type read.
     */
    public abstract String getName();

    /**
     * Gets the length of the binary data in bytes.
     * <p>Should only return the length for data inside the fixed-data part of a row, not variable-length data present after.</p>
     * @return The length of the binary data in bytes.
     */
    public abstract int getLength();

    /**
     * Gets the {@link Type} of the read values.
     * @return The {@link Type} of the read values.
     */
    public abstract Type getType();

    static {
        DataReaders = new HashMap<>();
    }

    public static DataReader getReader(int type) {
        DataReader reader;
        if ((reader = DataReaders.get(type)) == null) {
            throw new IllegalArgumentException("Unsupported data type " + String.format("%04Xh", type));
        }

        return reader;
    }

    public abstract Object read(byte[] buffer, Column col, IDataRow row);

    public abstract Object read(byte[] buffer, int offset);

    protected static int getFieldOffset(Column col, IDataRow row) {
        return row.getOffset() + col.getOffset();
    }
}
