package ffxiv.housim.saintcoinach.math;

public class Quad {
    short x;
    short y;
    short z;
    short w;

    public Quad(long value) {
        x = (short) value;
        y = (short)(value >> 16);
        z = (short)(value >> 32);
        w = (short)(value >> 48);
    }
}
