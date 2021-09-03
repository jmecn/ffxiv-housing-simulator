package ffxiv.housim.toolkit.captures;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TreeMap;

// m fl_1st fl_2nd 不同,wil和其他不同
// l fl_2nd wl_2nd 不同, wil,sea,fst,est各有風格
@Slf4j
public class CaptureReader {

    private static Geometry read(String name, List<String> lines) {

        int size = lines.size();
        short[] indexes = new short[size - 1];
        TreeMap<Short, Vertex> treeMap = new TreeMap<>();
        for (int i = 1; i < size; i++) {
            Vertex v = new Vertex(lines.get(i));
            Vertex exist = treeMap.get(v.index);
            if (exist != null) {
                // 比较两个顶点的数据是否一致
                if (!exist.equals(v)) {
                    log.warn("not equal:{}", v.index);
                }
            }
            treeMap.put(v.index, v);
            indexes[v.ib] = v.index;
        }
        Vertex[] vertices = treeMap.values().toArray(new Vertex[0]);

        int vCount = vertices.length;
        log.debug("ib:{}, triangle:{}, vertex:{}, data:{}", size - 1, size / 3, vCount, vertices[vCount - 1].index + 1);
        Preconditions.checkArgument(vCount == vertices[vCount - 1].index + 1);

        Geometry geom = new Geometry(name);
        geom.setMesh(buildMesh(indexes, vertices));

        try {
            BinaryExporter.getInstance().save(geom, new File("data/" + name + ".j3o"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geom;
    }

    private static Mesh buildMesh(short[] indexes, Vertex[] vertices) {

        Mesh mesh = new Mesh();

        int vertCount = vertices.length;
        float[] positions = new float[vertCount * 4];
        float[] normal = new float[vertCount * 4];
        float[] binormal = new float[vertCount * 4];
        float[] uv = new float[vertCount * 2];

        for (int i = 0; i < vertCount; i++) {
            Vertex v = vertices[i];
            positions[i * 4] = v.x;
            positions[i * 4 + 1] = v.y;
            positions[i * 4 + 2] = v.z;
            positions[i * 4 + 3] = v.w;

            normal[i * 4] = v.nx;
            normal[i * 4 + 1] = v.ny;
            normal[i * 4 + 2] = v.nz;
            normal[i * 4 + 3] = v.nw;

            binormal[i * 4] = v.bx;
            binormal[i * 4 + 1] = v.by;
            binormal[i * 4 + 2] = v.bz;
            binormal[i * 4 + 3] = v.bw;

            uv[i * 2] = v.u;
            uv[i * 2 + 1] = v.v;
        }

        mesh.setBuffer(VertexBuffer.Type.Position, 4, positions);
        mesh.setBuffer(VertexBuffer.Type.Normal, 4, normal);
        mesh.setBuffer(VertexBuffer.Type.Binormal, 4, binormal);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, uv);
        mesh.setBuffer(VertexBuffer.Type.Index, 1, indexes);

        mesh.setStatic();
        mesh.updateBound();
        mesh.updateCounts();

        return mesh;
    }

    public static void main(String[] args) {
        String[] files = {
                // 高脚孤丘
                "w1i1_fl_base", "w1i1_fl_1st", "w1i1_wl_base", "w1i1_wl_1st",
                "w1i2_fl_base", "w1i2_fl_1st", "w1i2_fl_2nd", "w1i2_wl_base", "w1i2_wl_1st", "w1i2_wl_2nd",
                "w1i3_fl_base", "w1i3_fl_1st", "w1i3_fl_2nd", "w1i3_wl_base", "w1i3_wl_1st", "w1i3_wl_2nd",
                "w1i4_fl_1st", "w1i4_wl_1st",
                // 海雾村
                "s1i1_fl_base", "s1i1_fl_1st", "s1i1_wl_base", "s1i1_wl_1st",
                "s1i2_fl_base", "s1i2_fl_1st", "s1i2_fl_2nd", "s1i2_wl_base", "s1i2_wl_1st", "s1i2_wl_2nd",
                "s1i3_fl_base", "s1i3_fl_1st", "s1i3_fl_2nd", "s1i3_wl_base", "s1i3_wl_1st", "s1i3_wl_2nd",
                "s1i4_fl_1st", "s1i4_wl_1st",
                // 薰衣草苗圃
                "f1i1_fl_base", "f1i1_fl_1st", "f1i1_wl_base", "f1i1_wl_1st",
                "f1i2_fl_base", "f1i2_fl_1st", "f1i2_fl_2nd", "f1i2_wl_base", "f1i2_wl_1st", "f1i2_wl_2nd",
                "f1i3_fl_base", "f1i3_fl_1st", "f1i3_fl_2nd", "f1i3_wl_base", "f1i3_wl_1st", "f1i3_wl_2nd",
                "f1i4_fl_1st", "f1i4_wl_1st",
                // 白银乡
                "e1i1_fl_base", "e1i1_fl_1st", "e1i1_wl_base", "e1i1_wl_1st",
                "e1i2_fl_base", "e1i2_fl_1st", "e1i2_fl_2nd", "e1i2_wl_base", "e1i2_wl_1st", "e1i2_wl_2nd",
                "e1i3_fl_base", "e1i3_fl_1st", "e1i3_fl_2nd", "e1i3_wl_base", "e1i3_wl_1st", "e1i3_wl_2nd",
                "e1i4_fl_1st", "e1i4_wl_1st",
        };

        for (String file : files) {

            try {
                URL url = Resources.getResource("Captures/" + file + ".csv");
                log.debug("load:{}", file);

                List<String> lines = Resources.readLines(url, StandardCharsets.UTF_8);
                read(file, lines);
            } catch (Exception e) {
                log.warn("NOT EXIST:{}", file);
            }

        }
    }
}
