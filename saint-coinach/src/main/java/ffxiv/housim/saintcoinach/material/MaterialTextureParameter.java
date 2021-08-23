package ffxiv.housim.saintcoinach.material;

import java.nio.ByteBuffer;

public class MaterialTextureParameter {
    public int parameterId;
    public short unknown1;
    public short unknown2;
    public int textureIndex;

    public MaterialTextureParameter(ByteBuffer buffer) {
        parameterId = buffer.getInt();
        unknown1 = buffer.getShort();
        unknown2 = buffer.getShort();
        textureIndex = buffer.getInt();
    }
}
