package ffxiv.housim.saintcoinach.db.ex.datareaders;

import ffxiv.housim.saintcoinach.db.ex.Column;
import ffxiv.housim.saintcoinach.db.ex.DataReader;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class BoolReader extends DataReader {
    @Override
    public String getName() {
        return "bool";
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public Type getType() {
        return boolean.class;
    }

    @Override
    public Object read(ByteBuffer buffer, Column col, IDataRow row) {
        int offset = getFieldOffset(col, row);
        buffer.position(offset);
        return buffer.get() != 0;
    }

    @Override
    public Object read(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        return buffer.get() != 0;
    }
}
