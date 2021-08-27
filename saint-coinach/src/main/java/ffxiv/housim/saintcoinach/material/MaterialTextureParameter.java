package ffxiv.housim.saintcoinach.material;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class MaterialTextureParameter {
    @Getter
    public int parameterId;
    public int alpha;
    public short unknown2;
    @Getter
    public int textureIndex;

    public float alphaDiscard;
    public MaterialTextureParameter(ByteBuffer buffer) {
        parameterId = buffer.getInt();
        alpha = buffer.getShort() & 0xFFFF;
        unknown2 = buffer.getShort();
        textureIndex = buffer.getInt();

        alphaDiscard = alpha / 64000f;
        log.info("alpha:{}, val={}, hex:{}", alphaDiscard, alpha);
    }
}
