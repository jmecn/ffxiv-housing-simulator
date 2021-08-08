package ffxiv.housim.saintcoinach;

public class XivQuad {
    short x;
    short y;
    short z;
    short w;
    public XivQuad(long value) {
        x = (short) value;
        y = (short)(value >> 16);
        z = (short)(value >> 32);
        w = (short)(value >> 48);
    }
}
