package ffxiv.housim.saintcoinach.math;

import java.nio.ByteBuffer;

public final class Vector4 {
    public final static Vector4 ZERO = new Vector4(0);
    public final static Vector4 ONE = new Vector4(1);

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4(float value) {
        x = y = z = w = value;
    }

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4(Vector2 xy, Vector2 zw) {
        this.x = xy.x;
        this.y = xy.y;
        this.z = zw.x;
        this.w = zw.y;
    }

    public Vector4(Vector3 v, float w) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public Vector4(ByteBuffer buffer) {
        x = buffer.getFloat();
        y = buffer.getFloat();
        z = buffer.getFloat();
        w = buffer.getFloat();
    }
}
