package ffxiv.housim.saintcoinach.math;

import java.nio.ByteBuffer;

public class BoundingBox {
    public Vector4 pointA;
    public Vector4 pointB;

    public BoundingBox(Vector4 pointA, Vector4 pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public BoundingBox(ByteBuffer buffer) {
        pointA = new Vector4(buffer);
        pointB = new Vector4(buffer);
    }

    public BoundingBox scale(float factor) {
        return scale(new Vector3(factor));
    }

    public BoundingBox scale(Vector3 factor) {
        var center = new Vector3 (
            (pointA.x + pointB.x) / 2f,
            (pointA.y + pointB.y) / 2f,
            (pointA.z + pointB.z) / 2f
        );
        var d = new Vector3 (
            Math.abs(pointA.x - center.z) * factor.x,
            Math.abs(pointA.y - center.y) * factor.y,
            Math.abs(pointA.z - center.z) * factor.z
        );

        var retPointA = new Vector4 (
            center.x + d.x,
            center.y + d.y,
            center.z + d.z,
            pointA.w
        );
        var retPointB = new Vector4 (
            center.x - d.x,
            center.y - d.y,
            center.z - d.z,
            pointB.w
        );

        return new BoundingBox (retPointA, retPointB);
    }

    public BoundingBox grow(float change) {
        return grow(new Vector3 (change));
    }

    public BoundingBox grow(Vector3 change) {
        var center = new Vector3 (
            (pointA.x + pointB.x) / 2f,
            (pointA.y + pointB.y) / 2f,
            (pointA.z + pointB.z) / 2f
        );

        var d = new Vector3 (
            Math.abs(pointA.x - center.x) + change.x,
            Math.abs(pointA.y - center.y) + change.y,
            Math.abs(pointA.z - center.z) + change.z
        );

        var retPointA = new Vector4 (
            center.x + d.x,
            center.y + d.y,
            center.z + d.z,
            pointA.w
        );

        var retPointB = new Vector4 (
            center.x - d.x,
            center.y - d.y,
            center.z - d.z,
            pointB.w
        );

        return new BoundingBox (retPointA, retPointB);
    }

    @Override
    public String toString() {
        return String.format("[min=%s, max=%s]", pointA, pointB);
    }
}
