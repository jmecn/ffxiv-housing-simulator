package ffxiv.housim.saintcoinach.ex.datareaders;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.DataReader;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class FloatReader extends DataReader<Float> {
    @Override
    public String getName() {
        return "float";
    }

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    public Type getType() {
        return float.class;
    }

    @Override
    public Float read(ByteBuffer buffer, Column col, IDataRow row) {
        int offset = getFieldOffset(col, row);
        buffer.position(offset);
        return buffer.getFloat();
    }

    @Override
    public Float read(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        return buffer.getFloat();
    }
}
