package ffxiv.housim.saintcoinach.math;

public final class XivQuad {
    public short x;
    public short y;
    public short z;
    public short w;

    public XivQuad(long value) {
        x = (short) value;
        y = (short)(value >> 16);
        z = (short)(value >> 32);
        w = (short)(value >> 48);
    }

    @Override
    public String toString() {
        return "XivQuad{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
