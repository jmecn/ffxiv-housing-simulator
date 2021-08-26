package ffxiv.housim.saintcoinach.material;

import lombok.Getter;

import java.nio.ByteBuffer;

public class MaterialTextureParameter {
    @Getter
    public int parameterId;
    public short unknown1;
    public short unknown2;
    @Getter
    public int textureIndex;

    public MaterialTextureParameter(ByteBuffer buffer) {
        parameterId = buffer.getInt();
        unknown1 = buffer.getShort();
        unknown2 = buffer.getShort();
        textureIndex = buffer.getInt();
    }
}
