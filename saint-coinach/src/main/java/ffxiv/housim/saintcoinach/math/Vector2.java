package ffxiv.housim.saintcoinach.math;

public final class Vector2 {
    public final static Vector2 ZERO = new Vector2(0, 0);
    public final static Vector2 ONE = new Vector2(1, 1);

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
