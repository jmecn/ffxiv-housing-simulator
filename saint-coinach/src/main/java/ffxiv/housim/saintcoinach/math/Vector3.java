package ffxiv.housim.saintcoinach.math;

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

}
