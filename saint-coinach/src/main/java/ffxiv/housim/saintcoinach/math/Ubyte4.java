package ffxiv.housim.saintcoinach.math;

import java.nio.ByteBuffer;

public final class Ubyte4 {
    public byte x;
    public byte y;
    public byte z;
    public byte w;

    public Ubyte4(ByteBuffer buffer) {
        x = buffer.get();
        y = buffer.get();
        z = buffer.get();
        w = buffer.get();
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %d)", x, y, z, w);
    }
}
