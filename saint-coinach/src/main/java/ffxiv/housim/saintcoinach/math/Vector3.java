package ffxiv.housim.saintcoinach.math;

import java.nio.ByteBuffer;

public final class Vector3 {
    public final static Vector3 ZERO = new Vector3(0);
    public final static Vector3 ONE = new Vector3(1);

    public float x;
    public float y;
    public float z;

    public Vector3(float val) {
        x = y = z= val;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3(ByteBuffer buffer) {
        x = buffer.getFloat();
        y = buffer.getFloat();
        z = buffer.getFloat();
    }
}
