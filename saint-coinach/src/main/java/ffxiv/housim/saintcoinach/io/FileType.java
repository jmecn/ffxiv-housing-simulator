package ffxiv.housim.saintcoinach.io;

import java.util.HashMap;
import java.util.Map;

public enum FileType {
    Unknown(0),
    Empty(1),
    Binary(2),
    Model(3),
    Image(4),
    ;
    private final int value;

    FileType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    private final static Map<Integer, FileType> CACHE = new HashMap<>();

    static {
        for (FileType e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static FileType of(Integer value) {
        if (!CACHE.containsKey(value)) {
            return Unknown;
        }
        return CACHE.get(value);
    }
}
