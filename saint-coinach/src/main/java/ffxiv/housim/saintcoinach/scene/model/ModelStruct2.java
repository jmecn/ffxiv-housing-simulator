package ffxiv.housim.saintcoinach.scene.model;

import java.nio.ByteBuffer;

public class ModelStruct2 {// 1 in hsl
    public int unknown1;
    public int unknown2;
    public int unknown3;
    public int unknown4;
    public int unknown5;

    ModelStruct2(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
        unknown4 = buffer.getInt();
        unknown5 = buffer.getInt();
    }
}
