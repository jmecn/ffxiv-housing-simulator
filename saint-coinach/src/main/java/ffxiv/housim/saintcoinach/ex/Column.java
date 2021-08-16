package ffxiv.housim.saintcoinach.ex;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * Class for represeting columns inside EX files.
 */
@Slf4j
public class Column {
    // Gets the Header of the EX file the column is in.
    @Getter
    protected Header header;
    // Gets the index of the column inside the EX file.
    @Getter
    protected int index;
    // Gets the integer identifier for the type of the column's data.
    // This value is read from the source header to get the correct object for <see cref="Reader" />, should not be required any further.
    @Getter
    protected int type;
    // Gets the position of the column's data in a row.
    @Getter
    protected int offset;
    // Gets the DataReader used to read column's data.
    @Getter
    protected DataReader reader;

    /**
     * Gets a string indicating what type the column's contents are.
     *
     * @return A string indicating what type the column's contents are
     */
    public String getValueType() {
        return reader.getName();
    }

    /**
     * Initializes a new instance of the {@link Column} class.
     *
     * @param header The {@link Header} of the EX file the column is in.
     * @param index The index of the column inside the EX file.
     * @param type The column value type
     * @param offset The column offset
     */
    public Column(Header header, int index, int type, int offset) {
        this.header = header;
        this.index = index;

        this.type = type;
        this.offset = offset;
        this.reader = DataReader.getReader(type);
    }

    /**
     * Read the column's value in a row, possibly post-processed depending on the column's implementation.
     *
     * @param buffer A byte-array containing the contents of the data file.
     * @param row The {@link IDataRow} whose data should be read.
     * @return Returns the column's value in <code>row</code>.
     */
    public Object read(ByteBuffer buffer, IDataRow row) {
        return reader.read(buffer, this, row);
    }

    public Object read(ByteBuffer buffer, IDataRow row, int offset) {
        return reader.read(buffer, offset);
    }

    /**
     * Read the raw column's value in a row.
     *
     * @param buffer A byte-array containing the contents of the data file.
     * @param row The {@link IDataRow} whose data should be read.
     * @return Returns the raw column's value in <code>row</code>.
     */
    public Object readRaw(ByteBuffer buffer, IDataRow row) {
        log.debug("type:{}, row:{}", type, row);
        return reader.read(buffer, this, row);
    }

    public Object readRaw(ByteBuffer buffer, IDataRow row, int offset) {
        return reader.read(buffer, offset);
    }
}