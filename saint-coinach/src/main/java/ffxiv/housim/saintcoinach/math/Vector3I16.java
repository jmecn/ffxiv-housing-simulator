package ffxiv.housim.saintcoinach.math;

import ffxiv.housim.saintcoinach.utils.HalfHelper;

import java.nio.ByteBuffer;

public final class Vector3I16 {
    public final static Vector3I16 ZERO = new Vector3I16(0);
    public final static Vector3I16 ONE = new Vector3I16(1);

    public short x;
    public short y;
    public short z;

    public float fx;
    public float fy;
    public float fz;

    public Vector3I16(int val) {
        x = y = z= (short) val;
    }

    public Vector3I16(int x, int y, int z) {
        this.x = (short) x;
        this.y = (short) y;
        this.z = (short) z;
    }

    public Vector3I16(ByteBuffer buffer) {
        x = buffer.getShort();
        y = buffer.getShort();
        z = buffer.getShort();
    }
}
