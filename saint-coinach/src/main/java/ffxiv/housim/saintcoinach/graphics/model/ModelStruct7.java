package ffxiv.housim.saintcoinach.graphics.model;

import java.nio.ByteBuffer;

public class ModelStruct7 {// 6 in hsl
    public short index;
    public short vertex;

    ModelStruct7(ByteBuffer buffer) {
        index = buffer.getShort();
        vertex = buffer.getShort();
    }
}
