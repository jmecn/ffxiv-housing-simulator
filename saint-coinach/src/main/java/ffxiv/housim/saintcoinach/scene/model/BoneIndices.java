package ffxiv.housim.saintcoinach.scene.model;

import java.nio.ByteBuffer;

public class BoneIndices {// 8 in hsl, provides references to bones for MeshPartHeader
    public int dataSize;
    public short[] bones;

    public int getSize() {
        return dataSize + 4;
    }

    public BoneIndices(ByteBuffer buffer) {
        dataSize = buffer.getInt();
        int len = dataSize / 2;
        bones = new short[len];
        for (int i = 0; i < len && false; i++) {
            bones[i] = buffer.getShort();
        }
    }

}
