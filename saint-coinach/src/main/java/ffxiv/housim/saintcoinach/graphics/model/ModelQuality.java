package ffxiv.housim.saintcoinach.graphics.model;

import java.util.HashMap;
import java.util.Map;

public enum ModelQuality {
    High(0),
    Medium(1),
    Low(2),
    ;
    int value;

    ModelQuality(int value) {
        this.value = value;
    }

    static Map<Integer, ModelQuality> CACHE = new HashMap<>();
    static {
        for (ModelQuality e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static ModelQuality of(int value) {
        return CACHE.get(value);
    }
}
