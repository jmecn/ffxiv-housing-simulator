package ffxiv.housim.saintcoinach.imaging;

import java.util.HashMap;
import java.util.Map;

public enum ImageFormat {
    Unknown(0),
    A16R16G16B16Float(0x2460),

    R3G3B2(0x1130),

    A4R4G4B4(0x1440),
    A1R5G5B5(0x1441),// RGB5A1

    A8R8G8B8_1(0x1131),// ARGB8,
    A8R8G8B8_2(0x1450),// ARGB8
    A8R8G8B8_Cube(0x1451),
    A8R8G8B8_4(0x2150),// ARGB8
    A8R8G8B8_5(0x4401),// ARGB8

    Dxt1(0x3420),
    Dxt3(0x3430),
    Dxt5(0x3431),



    ;

    private int value;

    private ImageFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private final static Map<Integer, ImageFormat> CACHE = new HashMap<>();

    static {
        for (ImageFormat e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static ImageFormat of(Integer value) {
        if (!CACHE.containsKey(value)) {
            return Unknown;
        }
        return CACHE.get(value);
    }
}
