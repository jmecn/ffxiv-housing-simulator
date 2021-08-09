package ffxiv.housim.saintcoinach.ex;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DataRow2 extends DataRowBase {
    final static int MetadataLength = 0x06;

    private boolean isRead = false;
    private final Map<Integer, SubRow> subRows = new HashMap<>();

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
        int o = offset;

        b.position(o);
        for(int i = 0; i < subRowCount; ++i) {
            int key = b.getShort();
            o += 2;

            SubRow r = new SubRow(this, key, o);
            subRows.put(key, r);

            o += h.getFixedSizeDataLength();
        }

        isRead = true;
    }
}
