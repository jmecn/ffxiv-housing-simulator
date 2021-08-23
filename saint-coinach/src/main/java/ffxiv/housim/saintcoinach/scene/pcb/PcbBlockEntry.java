package ffxiv.housim.saintcoinach.scene.pcb;

import ffxiv.housim.saintcoinach.math.IndexData;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector3I16;
import lombok.Getter;

import java.nio.ByteBuffer;

@Getter
public class PcbBlockEntry {

    private final PcbFile parent;
    private final int offset;

    private PcbBlockDataType type;
    private int blockSize; // when group size in bytes for the group block

    // bounding box
    private Vector3 min;
    private Vector3 max;

    private short verticesI16Count; // number of vertices packed into 16 bit
    private short indicesCount;     // number of indices
    private int verticesCount;      // number of normal float vertices

    private PcbBlockData data;

    PcbBlockEntry(PcbFile parent, ByteBuffer buffer, int offset) {
        this.parent = parent;
        this.offset = offset;
        buffer.position(offset);

        readHeader(buffer);

        data = new PcbBlockData();

        data.vertices = new Vector3[verticesCount];
        data.verticesI16 = new Vector3I16[verticesI16Count];
        data.indices = new IndexData[indicesCount];

        for (int i = 0; i < verticesCount; i++) {
            data.vertices[i] = new Vector3(buffer);
        }

        for (int i = 0; i < verticesI16Count; i++) {
            data.verticesI16[i] = new Vector3I16(buffer);
        }

        for (int i = 0; i < indicesCount; i++) {
            data.indices[i] = new IndexData(buffer);
        }
    }

    private void readHeader(ByteBuffer buffer) {
        type = PcbBlockDataType.of(buffer.getInt());
        blockSize = buffer.getInt();

        min = new Vector3(buffer);
        max = new Vector3(buffer);

        verticesI16Count = buffer.getShort();
        indicesCount = buffer.getShort();
        verticesCount = buffer.getInt();
    }
}
