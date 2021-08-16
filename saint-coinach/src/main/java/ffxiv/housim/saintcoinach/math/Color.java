package ffxiv.housim.saintcoinach.math;

public class Color {
    int value;

    public Color() {
        value = 0xff000000;
    }

    public Color(int rgb) {
        value = 0xff000000 | rgb;
    }

    public Color(int argb, boolean hasAlpha) {
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
