package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum HousingSize {
    co(254, "Common"),
    s(0, "Small"),
    m(1, "Medium"),
    l(2, "Large"),
    ;

    @Getter
    short value;

    @Getter
    String desc;

    HousingSize(int value, String desc) {
        this.value = (short) value;
        this.desc = desc;
    }

    final static Map<Short, HousingSize> CACHE = new HashMap<>();

    static {
        for(HousingSize e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static HousingSize of(short value) {
        return CACHE.get(value);
    }

}
