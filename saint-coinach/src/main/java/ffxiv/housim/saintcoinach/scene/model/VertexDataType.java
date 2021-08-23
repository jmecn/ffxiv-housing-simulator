package ffxiv.housim.saintcoinach.scene.model;

import java.util.HashMap;
import java.util.Map;

public enum VertexDataType {// byte

    Single3(0x02),
    Single4(0x03),
    UInt(0x05),
    ByteFloat4(0x08),
    Half2(0x0D),
    Half4(0x0E),

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
