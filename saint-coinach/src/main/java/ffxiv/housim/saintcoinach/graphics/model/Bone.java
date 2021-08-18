package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.math.Vector4;
import lombok.Getter;

import java.nio.ByteBuffer;

public class Bone {
    @Getter
    private ModelDefinition definition;
    @Getter
    private String name;
    @Getter
    private int index;
    @Getter
    private Vector4 unknown1;
    @Getter
    private Vector4 unknown2;

    Bone(ModelDefinition definition, int index, ByteBuffer buffer) {
        this.definition = definition;
        this.index = index;
        this.name = definition.getBoneNames()[index];

        this.unknown1 = new Vector4(buffer);
        this.unknown2 = new Vector4(buffer);
    }
}
