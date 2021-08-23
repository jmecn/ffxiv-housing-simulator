package ffxiv.housim.saintcoinach.math;

import ffxiv.housim.saintcoinach.db.xiv.XivName;

@XivName("Color")
public final class XivColor {
    int value;

    byte a, r ,g , b;

    public XivColor() {
        value = 0;
        a = r = g = b = 0;
    }

    public XivColor(int rgb) {
        value = 0xFF000000 | rgb;
        a = (byte) 0xFF;
        r = (byte) ((rgb >> 16) & 0xFF);
        g = (byte) ((rgb >> 8)  & 0xFF);
        b = (byte) ((rgb        & 0xFF));
    }

    public XivColor(int argb, boolean hasAlpha) {
        if (hasAlpha) {
            value = argb;
            a = (byte) ((argb >> 24) & 0xFF);
        } else {
            value = 0xFF000000 | argb;
            a = (byte) 0xFF;
        }

        r = (byte) ((argb >> 16) & 0xFF);
        g = (byte) ((argb >> 8)  & 0xFF);
        b = (byte) ((argb        & 0xFF));
    }

    public XivColor(byte a, byte r, byte g, byte b) {
        this.value = (0xFF & a) << 24
                   | (0xFF & r) << 16
                   | (0xFF & g) << 8
                   | (0xFF & b);
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public String toString() {
        return String.format("#%08X", value);
    }
}
