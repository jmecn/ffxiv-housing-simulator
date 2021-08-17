package ffxiv.housim.saintcoinach.ex.datareaders;

import ffxiv.housim.saintcoinach.math.XivQuad;
import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.DataReader;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class XivQuadReader extends DataReader {

    @Override
    public String getName() {
        return "int64";
    }

    @Override
    public int getLength() {
        return 8;
    }

    @Override
    public Type getType() {
        return XivQuad.class;
    }

    @Override
    public Object read(ByteBuffer buffer, Column col, IDataRow row) {
        int offset = getFieldOffset(col, row);
        buffer.position(offset);
        return new XivQuad(buffer.getLong());
    }

    @Override
    public Object read(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        return new XivQuad(buffer.getLong());
    }
}
