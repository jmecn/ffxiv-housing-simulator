package ffxiv.housim.saintcoinach.graphics.mesh;

import ffxiv.housim.saintcoinach.graphics.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.graphics.model.*;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Mesh {
    public final static byte BytesPerIndex = 2;   // TODO: 99.999% sure this is constant, but you never know.

    @Getter
    private Model model;
    @Getter
    private MeshHeader header;
    @Getter
    private VertexFormat vertexFormat;
    @Getter
    private int index;
    @Getter
    private Vertex[] vertices;
    @Getter
    private short[] indices;
    @Getter
    private MeshPart[] parts;
    @Getter
    private MaterialDefinition material;

    public Mesh(Model model, int index, byte[] vertexBuffer, byte[] indexBuffer) {
        this.model = model;
        this.index = index;

        ModelDefinition def = model.getDefinition();

        this.header = def.getMeshHeaders()[index];
        this.vertexFormat = def.getVertexFormats()[index];
        this.material = def.getMaterials()[index];

        this.parts = new MeshPart[header.partCount];
        for (int i = 0; i < header.partCount; i++) {
            this.parts[i] = new MeshPart(this, def.getMeshPartHeaders()[header.partOffset + i], indexBuffer);
        }

        readVertices(vertexBuffer);
        readIndices(indexBuffer);
    }

    private void readVertices(byte[] data) {
        int[] offsets = new int[header.vertexBufferCount];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = header.vertexOffsets[i];
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        vertices = new Vertex[header.vertexCount];
        for (var i = 0; i < header.vertexCount; i++) {
            vertices[i] = VertexReader.read(buffer, vertexFormat, offsets);

            for (var oi = 0; oi < offsets.length; oi++) {
                offsets[oi] += header.bytesPerVertexPerBuffer[oi];
            }
        }
    }

    private void readIndices(byte[] data) {
        int position = header.indexBufferOffset * BytesPerIndex;
        this.indices = new short[header.indexCount];

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(position);
        for (var i = 0; i < header.indexCount; i++) {
            this.indices[i] = buffer.getShort();
        }
    }
}
