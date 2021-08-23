package ffxiv.housim.saintcoinach.graphics.shpk;

import lombok.Getter;

import java.nio.ByteBuffer;

public class ShaderParameterReference {
    public final static int SIZE = 10;

    @Getter
    private ParameterType type;
    @Getter
    private int id;

    public ShaderParameterReference(ParameterType type, ByteBuffer buffer) {
        this.type = type;

        this.id = buffer.getInt();
        // skip 12 bytes
        int c0 = buffer.getInt();
        int c1 = buffer.getInt();
        int c2 = buffer.getInt();

    }
}
