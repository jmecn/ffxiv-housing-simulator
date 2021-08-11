package ffxiv.housim.saintcoinach.ex;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DataRow2 extends DataRowBase {
    final static int MetadataLength = 0x06;

    private boolean isRead = false;
    private final Map<Integer, SubRow> subRows = new TreeMap<>();

    @Getter
    private final int length;
    @Getter
    private final int subRowCount;

    protected DataRow2(IDataSheet sheet, int key, int offset) {
        super(sheet, key, offset + MetadataLength);

        ByteBuffer buffer = sheet.getBuffer();
        if (buffer.limit() < offset + MetadataLength) {
            throw new IndexOutOfBoundsException();
        }

        buffer.position(offset);
        length = buffer.getInt();
        subRowCount = buffer.getShort();
    }

    public Collection<Integer> getSubRowKeys() {
        if (!isRead) {
            read();
        }

        return subRows.keySet();
    }

    public Collection<SubRow> getSubRows() {
        if (!isRead) {
            read();
        }
        return subRows.values();
    }

    public SubRow getSubRow(int key) {
        if (!isRead) {
            read();
        }
        return subRows.get(key);
    }

    protected void read() {
        subRows.clear();

        Header h = sheet.getHeader();
        ByteBuffer b = sheet.getBuffer();
        int offset = this.offset;

        b.position(offset);
        for(int i = 0; i < subRowCount; ++i) {
            int key = b.getShort();
            offset += 2;

            SubRow r = new SubRow(this, key, offset);
            subRows.put(key, r);

            offset += h.getFixedSizeDataLength();
        }

        isRead = true;
    }

    @Override
    public Object get(int columnIndex) {
        throw new UnsupportedOperationException("Cannot get column on Variant 2 DataRow. Use getSubRow instead.");
    }

    @Override
    public java.lang.Object getRaw(int columnIndex) {
        throw new UnsupportedOperationException("Cannot get column on Variant 2 DataRow. Use getSubRow instead.");
    }
}