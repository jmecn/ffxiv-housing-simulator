package ffxiv.housim.saintcoinach.scene.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ffxiv.housim.saintcoinach.io.Hash;
import ffxiv.housim.saintcoinach.scene.mesh.Mesh;
import lombok.Getter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    private String name;

    static Cache<Integer, Mesh> CACHE;
    static {
        CACHE = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(3600))
                .softValues()
                .build();
    }

    public Model(ModelDefinition definition, ModelQuality quality) throws IOException {
        this.definition = definition;
        this.quality = quality;
        this.header = definition.getModelHeaders()[quality.value];

        byte[] vertexBuffer = definition.getFile().getPart(VertexPartOffset + quality.value);
        byte[] indexBuffer = definition.getFile().getPart(IndexPartOffset + quality.value);

        String name = getName();// using to calculate hash

        this.meshes = new Mesh[header.meshCount];
        for (var i = 0; i < header.meshCount; i++) {
            int index = header.meshOffset + i;

            int hash = Hash.compute(name + "#" + index);

            Mesh mesh;
            try {
                mesh = CACHE.get(hash, () -> {
                    Mesh m = new Mesh(this, index, vertexBuffer, indexBuffer);
                    m.setHash(hash);
                    return m;
                });
            } catch (ExecutionException e) {
                mesh = new Mesh(this, index, vertexBuffer, indexBuffer);
                mesh.setHash(hash);
                e.printStackTrace();
            }

            meshes[i] = mesh;
        }
    }

    public String getName() {
        if (name == null) {
            name = definition.getFile().getPath() + "#" + quality.name();
        }
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return Hash.compute(getName());
    }
}