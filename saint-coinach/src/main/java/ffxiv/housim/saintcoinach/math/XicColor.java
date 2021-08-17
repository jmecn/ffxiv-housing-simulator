package ffxiv.housim.saintcoinach.math;

import ffxiv.housim.saintcoinach.xiv.XivName;

@XivName("Color")
public final class XicColor {
    int value;

    public XicColor() {
        value = 0xff000000;
    }

    public XicColor(int rgb) {
        value = 0xff000000 | rgb;
    }

    public XicColor(int argb, boolean hasAlpha) {
        if (hasAlpha) {
            value = argb;
        } else {
            value = 0xff000000 | argb;
        }
    }

    @Override
    public String toString() {
        return String.format("#%08X", value);
    }
}
