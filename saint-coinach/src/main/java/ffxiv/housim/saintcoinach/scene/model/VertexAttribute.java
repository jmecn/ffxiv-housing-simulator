package ffxiv.housim.saintcoinach.scene.model;

import java.util.HashMap;
import java.util.Map;

public enum VertexAttribute {// byte

    Position(0x00),
    BlendWeights(0x01),
    BlendIndices(0x02),
    Normal(0x03),
    UV(0x04),
    Tangent2(0x05),// Don't ask me why the second one (only present on dual-textured models) is first
    Tangent1(0x06),
    Color(0x07),

    // TODO: Check for additional types (chara/* models checked, most in bg/* too, but some couldn't be read)
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
