package ffxiv.housim.saintcoinach.scene.model;

import java.util.HashMap;
import java.util.Map;

public enum VertexDataType {

    Float1(0x00),
    Float2(0x01),
    Float3(0x02),
    Float4(0x03),
    Ubyte4(0x05),
    Short2(0x06),
    Short4(0x07),
    Ubyte4n(0x08),//Ubyte4 normalize
    Short2n(0x09),
    Short4n(0x0A),
    Half2(0x0D),
    Half4(0x0E),
    Compress(0x0F),

    ;

    private byte value;
    VertexDataType(int value) {
        this.value = (byte) value;
    }

    private final static Map<Byte, VertexDataType> CACHE = new HashMap<>();
    static {
        for (VertexDataType e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static VertexDataType of(Byte value) {
        return CACHE.get(value);
    }
}
