package ffxiv.housim.saintcoinach.sound;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ScdOggCryptType {
    NONE(0x0000),
    VARBIS_HEADER_XOR(0x2002),
    FULL_XOR_USING_TABLE(0x2003);

    private final short value;

    ScdOggCryptType(int value) {
        this.value = (short) value;
    }

    private final static Map<Short, ScdOggCryptType> CACHE;

    static {
        Map<Short, ScdOggCryptType> map = new HashMap<>();
        for (ScdOggCryptType it : values()) {
            map.put(it.value, it);
        }
        CACHE = Collections.unmodifiableMap(map);
    }

    public static ScdOggCryptType of(short value) {
        return CACHE.get(value);
    }
}
