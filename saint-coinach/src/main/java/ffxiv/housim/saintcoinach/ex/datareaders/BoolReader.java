package ffxiv.housim.saintcoinach.ex.datareaders;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.DataReader;
import ffxiv.housim.saintcoinach.ex.row.IDataRow;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class BoolReader extends DataReader<Boolean> {
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
    public Boolean read(ByteBuffer buffer, Column col, IDataRow row) {
        int offset = getFieldOffset(col, row);
        buffer.position(offset);
        return buffer.get() != 0;
    }

    @Override
    public Boolean read(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        return buffer.get() != 0;
    }
}
