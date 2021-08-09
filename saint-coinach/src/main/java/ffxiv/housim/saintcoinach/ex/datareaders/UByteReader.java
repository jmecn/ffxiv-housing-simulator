package ffxiv.housim.saintcoinach.ex.datareaders;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.DataReader;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class UByteReader extends DataReader {
    @Override
    public String getName() {
        return "byte";
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public Type getType() {
        return short.class;
    }

    @Override
    public Object read(ByteBuffer buffer, Column col, IDataRow row) {
        int offset = getFieldOffset(col, row);
        buffer.position(offset);
        return (short) (buffer.get() & 0xFF);
    }

    @Override
    public Object read(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        return (short) (buffer.get() & 0xFF);
    }
}
