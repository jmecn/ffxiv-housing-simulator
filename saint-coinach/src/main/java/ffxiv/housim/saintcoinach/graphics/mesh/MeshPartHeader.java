package ffxiv.housim.saintcoinach.graphics.mesh;

import java.nio.ByteBuffer;

public class MeshPartHeader {
    public int indexOffset;
    public int indexCount;
    public int attributesMask;
    public short boneReferenceOffset; // In ModelStruct8.Bones
    public short boneReferenceCount;  // In ModelStruct8.Bones

    public MeshPartHeader(ByteBuffer buffer) {
        indexOffset = buffer.getInt();
        indexCount = buffer.getInt();
        attributesMask = buffer.getInt();
        boneReferenceOffset = buffer.getShort();
        boneReferenceCount = buffer.getShort();
    }
}
