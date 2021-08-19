package ffxiv.housim.saintcoinach.graphics.model;

import java.nio.ByteBuffer;

public class BoneList { // 3 in hsl, something to do with bones (hierarchy? though it looks nothing like a hierarchy)
    public short[] bones = new short[0x40];
    public int actualCount;

    public BoneList(ByteBuffer buffer) {
        for(int i = 0; i < 0x40; i++) {
            bones[i] = buffer.getShort();
        }
        actualCount = buffer.getInt();
    }
}
