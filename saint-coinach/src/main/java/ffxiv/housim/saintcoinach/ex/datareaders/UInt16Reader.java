package ffxiv.housim.saintcoinach.ex.datareaders;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.DataReader;
import ffxiv.housim.saintcoinach.ex.row.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class UInt16Reader extends DataReader<Integer> {
    @Override
    public String getName() {
        return "uint16";
    }

    @Override
    public int getLength() {
        return 2;
    }

    @Override
    public Type getType() {
        return short.class;
    }

    @Override
    public Integer read(ByteBuffer buffer, Column col, IDataRow row) {
        int offset = getFieldOffset(col, row);
        buffer.position(offset);
        return buffer.getShort() & 0xFFFF;
    }

    @Override
    public Integer read(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        return buffer.getShort() & 0xFFFF;
    }
}
