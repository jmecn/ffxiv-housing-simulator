package ffxiv.housim.saintcoinach.ex.datareaders;

import ffxiv.housim.saintcoinach.XivString;
import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.DataReader;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public final class XivStringReader extends DataReader {
    @Override
    public String getName() {
        return "str";
    }

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    public Type getType() {
        return String.class;
    }

    @Override
    public Object read(ByteBuffer buffer, Column col, IDataRow row) {
        int fieldOffset = getFieldOffset(col, row);
        int endOfFixed = row.getOffset() + row.getSheet().getHeader().getFixedSizeDataLength();

        buffer.position(fieldOffset);
        int start = endOfFixed + buffer.getInt();
        if (start < 0) {
            return null;
        }

        int end = start;
        while(end < buffer.limit() && buffer.get(end) != 0) {
            end++;
        }
        int len = end - start;
        if (len == 0) {
            return "";
        }

        byte[] bytes = new byte[len];
        buffer.position(start);
        buffer.get(bytes);

        return XivString.parseFFXIVString(bytes);
    }

    @Override
    public Object read(ByteBuffer buffer, int offset) {
        throw new UnsupportedOperationException();
    }
}
