package ffxiv.housim.saintcoinach.scene.model;

import java.nio.ByteBuffer;

public class ModelHeader {
    public short meshOffset;
    public short meshCount;
    public short[] unknown1 = new short[0x14];
    public int vertexDataSize;
    public int indexDataSize;
    public short[] unknown2 = new short[0x04];

    ModelHeader(ByteBuffer buffer) {
        meshOffset = buffer.getShort();
        meshCount = buffer.getShort();
        for (int i = 0; i < unknown1.length; i++) {
            unknown1[i] = buffer.getShort();
        }
        vertexDataSize = buffer.getInt();
        indexDataSize = buffer.getInt();
        for (int i = 0; i < unknown2.length; i++) {
            unknown2[i] = buffer.getShort();
        }
    }
}
