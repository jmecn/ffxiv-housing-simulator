package ffxiv.housim.saintcoinach.scene.model;

import java.nio.ByteBuffer;

public class ModelStruct1 {// 0 in hsl
    public int unknown1;
    public int unknown2;
    public int unknown3;
    public int unknown4;
    public int unknown5;
    public int unknown6;
    public int unknown7;
    public int unknown8;

    ModelStruct1(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
        unknown4 = buffer.getInt();
        unknown5 = buffer.getInt();
        unknown6 = buffer.getInt();
        unknown7 = buffer.getInt();
        unknown8 = buffer.getInt();
    }
}
