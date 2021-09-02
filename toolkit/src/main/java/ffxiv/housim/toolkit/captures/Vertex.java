package ffxiv.housim.toolkit.captures;

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
}
