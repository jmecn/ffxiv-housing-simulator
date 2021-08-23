package ffxiv.housim.saintcoinach.scene.model;

import ffxiv.housim.saintcoinach.scene.mesh.Mesh;
import lombok.Getter;

import java.io.IOException;

public class Model {
    final static int VertexPartOffset = 2;
    final static int IndexPartOffset = 8;

    @Getter
    private ModelDefinition definition;
    @Getter
    private ModelQuality quality;
    @Getter
    private ModelHeader header;
    @Getter
    private Mesh[] meshes;

    public Model(ModelDefinition definition, ModelQuality quality) throws IOException {
        this.definition = definition;
        this.quality = quality;
        this.header = definition.getModelHeaders()[quality.value];

        byte[] vertexBuffer = definition.getFile().getPart(VertexPartOffset + quality.value);
        byte[] indexBuffer = definition.getFile().getPart(IndexPartOffset + quality.value);

        this.meshes = new Mesh[header.meshCount];
        for (var i = 0; i < header.meshCount; i++) {
            var mesh = new Mesh(this, header.meshOffset + i, vertexBuffer, indexBuffer);

            meshes[i] = mesh;
        }
    }
}
