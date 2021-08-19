package ffxiv.housim.saintcoinach.graphics.model;

import java.nio.ByteBuffer;

public class ModelStruct5 {// 4 in hsl
    public int unknown1;
    public int unknown2;
    public int unknown3;
    public int unknown4;

    ModelStruct5(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
        unknown4 = buffer.getInt();
    }
}
