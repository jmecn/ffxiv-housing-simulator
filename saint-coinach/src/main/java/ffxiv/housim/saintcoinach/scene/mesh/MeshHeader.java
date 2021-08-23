package ffxiv.housim.saintcoinach.scene.mesh;

import java.nio.ByteBuffer;

public class MeshHeader {
    public int vertexCount;
    public int indexCount;
    public short materialIndex;
    public short partOffset;
    public short partCount;
    public short boneListIndex;
    public int indexBufferOffset;
    public int[] vertexOffsets = new int[3];
    public byte[] bytesPerVertexPerBuffer = new byte[3];
    public byte vertexBufferCount;

    public MeshHeader(ByteBuffer buffer) {
        vertexCount = buffer.getInt();
        indexCount = buffer.getInt();
        materialIndex = buffer.getShort();
        partOffset = buffer.getShort();
        partCount = buffer.getShort();
        boneListIndex = buffer.getShort();
        indexBufferOffset = buffer.getInt();
        for (int i = 0; i < vertexOffsets.length; i++) {
            vertexOffsets[i] = buffer.getInt();
        }
        for (int i = 0; i < bytesPerVertexPerBuffer.length; i++) {
            bytesPerVertexPerBuffer[i] = buffer.get();
        }
        vertexBufferCount = buffer.get();
    }
}
