package ffxiv.housim.saintcoinach.graphics.shpk;

import lombok.Getter;

import java.nio.ByteBuffer;

public class ParameterHeader {
    public final static int SIZE = 0x10;

    @Getter
    private ParameterType type;
    @Getter
    private int id;
    @Getter
    private int nameOffset;
    @Getter
    private int nameLength;

    public ParameterHeader(ParameterType type, ByteBuffer buffer) {
        this.type = type;

        this.id = buffer.getInt();
        this.nameOffset = buffer.getInt();
        this.nameLength = buffer.getInt();
        int unknown = buffer.getInt();// skip 4 bytes padding
    }
}
