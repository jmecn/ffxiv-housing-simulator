package ffxiv.housim.toolkit.captures;

import java.util.Objects;

public class Vertex {

    String[] args;

    // index
    int ib;// index buffer offset
    short index;// vertex index
    short indexBase;// ignore

    // 0-position
    float x, y, z, w;

    // 1-normal
    float nx, ny, nz, nw;

    // 2-binormal
    float bx, by, bz, bw;// 127 255 127 255

    // 3-uv
    float u, v;

    final static float R = 1f / 255;

    Vertex(String line) {
        args = line.split(",");

        ib = i(0);
        index = s(1);
        indexBase = s(2);

        x = f(3);
        y = f(4);
        z = f(5);
        w = f(6);

        nx = f(7);
        ny = f(8);
        nz = f(9);
        nw = f(10);

        bx = bf(11);
        by = bf(12);
        bz = bf(13);
        bw = bf(14);

        u = f(15);
        v = f(16);
    }

    int i(int index) {
        return Integer.parseInt(args[index]);
    }

    short s(int index) {
        return Short.parseShort(args[index]);
    }

    float f(int index) {
        return Float.parseFloat(args[index]);
    }
    byte b(int index) {
        return (byte) Integer.parseInt(args[index]);
    }

    float bf(int index) {
        return Integer.parseInt(args[index]) * R;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "index=" + index +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                ", nx=" + nx +
                ", ny=" + ny +
                ", nz=" + nz +
                ", nw=" + nw +
                ", bx=" + bx +
                ", by=" + by +
                ", bz=" + bz +
                ", bw=" + bw +
                ", u=" + u +
                ", v=" + v +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return index == vertex.index && Float.compare(vertex.x, x) == 0 && Float.compare(vertex.y, y) == 0 && Float.compare(vertex.z, z) == 0 && Float.compare(vertex.w, w) == 0 && Float.compare(vertex.nx, nx) == 0 && Float.compare(vertex.ny, ny) == 0 && Float.compare(vertex.nz, nz) == 0 && Float.compare(vertex.nw, nw) == 0 && Float.compare(vertex.bx, bx) == 0 && Float.compare(vertex.by, by) == 0 && Float.compare(vertex.bz, bz) == 0 && Float.compare(vertex.bw, bw) == 0 && Float.compare(vertex.u, u) == 0 && Float.compare(vertex.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, x, y, z, w, nx, ny, nz, nw, bx, by, bz, bw, u, v);
    }
}
