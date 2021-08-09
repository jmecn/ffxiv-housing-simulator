package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ex.datareaders.*;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class used in reading data from EX data files.
 */
public abstract class DataReader {
    /**
     * Mappings of type identifiers used in EX headers to their corresponding {@link DataReader}.
     */
    private final static Map<Integer, DataReader> DataReaders = new ConcurrentHashMap<>();

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
        DataReaders.put(0x00, new XivStringReader());
        DataReaders.put(0x01, new BoolReader());
        DataReaders.put(0x02, new ByteReader());
        DataReaders.put(0x03, new UByteReader());
        DataReaders.put(0x04, new Int16Reader());
        DataReaders.put(0x05, new UInt16Reader());
        DataReaders.put(0x06, new Int32Reader());
        DataReaders.put(0x07, new UInt32Reader());
        DataReaders.put(0x09, new FloatReader());
        DataReaders.put(0x0B, new XivQuadReader());

        for (byte i = 0; i < 8; i++) {
            DataReaders.put(0x19 + i, new MaskedBoolReader((byte)(1 << i)));
        }
    }

    public static DataReader getReader(int type) {
        DataReader reader = DataReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Unsupported data type " + String.format("%04Xh", type));
        }

        return reader;
    }

    public abstract Object read(ByteBuffer buffer, Column col, IDataRow row);

    public abstract Object read(ByteBuffer buffer, int offset);

    protected static int getFieldOffset(Column col, IDataRow row) {
        return row.getOffset() + col.getOffset();
    }
}
