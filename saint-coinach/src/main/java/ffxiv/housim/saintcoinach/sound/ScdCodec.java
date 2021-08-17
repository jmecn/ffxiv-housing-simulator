package ffxiv.housim.saintcoinach.sound;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ScdCodec {
    NONE(0x00),
    OGG(0x06),
    MSADPCM(0x0C);

    private final int value;

    ScdCodec(int value) {
        this.value = value;
    }

    private final static Map<Integer, ScdCodec> CACHE;

    static {
        Map<Integer, ScdCodec> map = new HashMap<>();
        for (ScdCodec it : values()) {
            map.put(it.value, it);
        }
        CACHE = Collections.unmodifiableMap(map);
    }

    public static ScdCodec of(int value) {
        return CACHE.get(value);
    }
}
