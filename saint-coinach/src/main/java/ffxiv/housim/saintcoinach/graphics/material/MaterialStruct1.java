package ffxiv.housim.saintcoinach.graphics.material;

import java.nio.ByteBuffer;

public class MaterialStruct1 {
    public int unknown1;   // Probably an Id of some sort
    public int unknown2;

    public MaterialStruct1(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        unknown2 = buffer.getInt();
    }
}
