package ffxiv.housim.saintcoinach.material;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

public class MaterialTextureParameter {
    @Getter
    public int parameterId;
    public float alphaDiscard;
    public short unknown2;
    @Getter
    public int textureIndex;

    public MaterialTextureParameter(ByteBuffer buffer) {
        parameterId = buffer.getInt();
        alphaDiscard = (buffer.getShort() & 0xFFFF) / 64000f;
        unknown2 = buffer.getShort();
        textureIndex = buffer.getInt();
    }
}
