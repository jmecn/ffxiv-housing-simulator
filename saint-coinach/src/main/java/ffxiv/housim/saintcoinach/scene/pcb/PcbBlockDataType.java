package ffxiv.housim.saintcoinach.scene.pcb;

import java.util.HashMap;
import java.util.Map;

public enum PcbBlockDataType {
    Entry(0),
    Group(0x30),
    ;
    private final int value;

    PcbBlockDataType(int value) {
        this.value = value;
    }

    static Map<Integer, PcbBlockDataType> CACHE = new HashMap<>();
    static {
        for (PcbBlockDataType e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static PcbBlockDataType of(int value) {
        return CACHE.get(value);
    }
}
