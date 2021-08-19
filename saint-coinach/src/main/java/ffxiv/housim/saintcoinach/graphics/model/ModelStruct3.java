package ffxiv.housim.saintcoinach.graphics.model;

import java.nio.ByteBuffer;

public class ModelStruct3 {// 7 in hsl
    public int unknown1;
    public int unknown2;
    public int unknown3;

    ModelStruct3(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
    }
}
