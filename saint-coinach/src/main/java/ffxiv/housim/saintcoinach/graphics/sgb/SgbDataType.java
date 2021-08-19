package ffxiv.housim.saintcoinach.graphics.sgb;

import java.util.HashMap;
import java.util.Map;

public enum SgbDataType {
    Unknown0008(0x0008),
    Group(0x0100),
    ;
    int value;

    SgbDataType(int value) {
        this.value = value;
    }

    static Map<Integer, SgbDataType> CACHE = new HashMap<>();
    static {
        for (SgbDataType e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static SgbDataType of(int value) {
        return CACHE.get(value);
    }
}
