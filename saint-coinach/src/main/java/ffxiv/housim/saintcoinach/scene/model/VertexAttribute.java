package ffxiv.housim.saintcoinach.scene.model;

import java.util.HashMap;
import java.util.Map;

public enum VertexAttribute {// byte

    Position(0x00),
    BoneWeights(0x01),
    BoneIndices(0x02),
    Normal(0x03),
    TexCoord(0x04),
    Tangent(0x05),
    Binormal(0x06),
    Color(0x07),

    ;

    private byte value;
    VertexAttribute(int value) {
        this.value = (byte) value;
    }

    private final static Map<Byte, VertexAttribute> CACHE = new HashMap<>();
    static {
        for (VertexAttribute e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static VertexAttribute of(Byte value) {
        return CACHE.get(value);
    }
}
