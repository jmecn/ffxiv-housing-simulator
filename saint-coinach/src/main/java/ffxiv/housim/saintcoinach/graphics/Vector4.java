package ffxiv.housim.saintcoinach.graphics;

public final class Vector4 {
    public final static Vector4 ZERO = new Vector4(0, 0, 0, 0);
    public final static Vector4 ONE = new Vector4(1, 1, 1, 1);

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

}
