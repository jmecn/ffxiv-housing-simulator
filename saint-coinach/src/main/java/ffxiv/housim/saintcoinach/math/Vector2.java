package ffxiv.housim.saintcoinach.math;

import java.nio.ByteBuffer;

public final class Vector2 {
    public final static Vector2 ZERO = new Vector2(0);
    public final static Vector2 ONE = new Vector2(1);

    public float x;
    public float y;

    public Vector2(float value) {
        x = y = value;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(ByteBuffer buffer) {
        x = buffer.getFloat();
        y = buffer.getFloat();
    }
}
