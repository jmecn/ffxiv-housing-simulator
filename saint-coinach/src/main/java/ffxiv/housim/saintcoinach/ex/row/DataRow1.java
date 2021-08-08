package ffxiv.housim.saintcoinach.ex.row;

import ffxiv.housim.saintcoinach.ex.sheet.IDataSheet;
import lombok.Getter;

import java.nio.ByteBuffer;

public class DataRow1 extends DataRowBase {
    final static int MetadataLength = 0x06;

    @Getter
    private int length;
    @Getter
    private int subRowCount;

    public DataRow1(IDataSheet sheet, int key, int offset) {
        super(sheet, key, offset + MetadataLength);

        ByteBuffer buffer = sheet.getBuffer();
        if (buffer.limit() < offset + MetadataLength) {
            throw new IndexOutOfBoundsException();
        }

        buffer.position(offset);
        length = buffer.getInt();
        subRowCount = buffer.getShort();
        if (subRowCount != 1) {
            throw new IllegalStateException();
        }
    }
}
