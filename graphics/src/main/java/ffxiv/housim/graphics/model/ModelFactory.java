package ffxiv.housim.graphics.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import ffxiv.housim.saintcoinach.math.Ubyte4;
import ffxiv.housim.saintcoinach.scene.model.*;
import ffxiv.housim.saintcoinach.scene.sgb.*;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector4;
import ffxiv.housim.saintcoinach.scene.terrain.Terrain;
import ffxiv.housim.saintcoinach.scene.terrain.Territory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelFactory {

    @Setter
    static PackCollection packs;
    @Setter
    static AssetManager assetManager;

    public static Node load(String sgbPath) {

        PackFile file = packs.tryGetFile(sgbPath);
        SgbFile sgbFile = new SgbFile(file);

        Node root = new Node(sgbFile.getFile().getPath());
        SgbGroup data = (SgbGroup) sgbFile.getData()[0];

        int models = 0;
        int chairs = 0;
        int targets = 0;
        for (ISgbEntry e : data.getEntries()) {

            if (e instanceof SgbEntryModel me) {
                build(root, me, models++);
            } else if (e instanceof SgbEntryChairMarker ce) {
                build(root, ce, chairs++);
            } else if (e instanceof SgbEntryTargetMarker te) {
                build(root, te, targets++);
            } else {
                log.warn("unsupported entry:{}", e);
            }
        }

        return root;
    }

    private static void build(Node root, SgbEntryTargetMarker tc, int targets) {

        ColorRGBA color = new ColorRGBA(0f, 1f, 0f, 1f);

        Geometry mark = new Geometry("TargetMarker#" + targets);
        mark.setMesh(new Sphere(8, 4, 0.1f));
        mark.setMaterial(MaterialFactory.colorMaterial(color));

        // transform
        Vector3 trans = tc.getTranslation();
        Vector3 scale = tc.getScale();
        Vector3 rotate = tc.getRotation();

        mark.setLocalTranslation(trans.x, trans.y, trans.z);
        mark.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        mark.setLocalScale(scale.x, scale.y, scale.z);

        //root.attachChild(mark);
    }

    private static void build(Node root, SgbEntryChairMarker ce, int chairs) {
        ColorRGBA color = new ColorRGBA(1.0f, 0f, 0f, 1f);

        Geometry mark = new Geometry("ChairMarker#" + chairs);
        mark.setMesh(new Sphere(8, 4, 0.1f));
        mark.setMaterial(MaterialFactory.colorMaterial(color));

        // transform
        Vector3 trans = ce.getTranslation();
        Vector3 scale = ce.getScale();
        Vector3 rotate = ce.getRotation();

        mark.setLocalTranslation(trans.x, trans.y, trans.z);
        mark.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        mark.setLocalScale(scale.x, scale.y, scale.z);

        root.attachChild(mark);
    }

    private static void build(Node root, SgbEntryModel me, int models) {
        build(root, me.getModel(), models);
    }

    private static void build(Node root, TransformedModel transformedModel, int models) {
        if (transformedModel == null) {
            return;
        }

        Node thisNode = new Node("#" + models);
        // transform
        Vector3 trans = transformedModel.getTranslation();
        Vector3 rotate = transformedModel.getRotation();
        Vector3 scale = transformedModel.getScale();
        log.debug("trans:{}, rotate:{}, scale:{}", trans, rotate, scale);

        thisNode.setLocalTranslation(trans.x, trans.y, trans.z);
        thisNode.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        thisNode.setLocalScale(scale.x, scale.y, scale.z);

        // model
        ModelDefinition modelDefinition = transformedModel.getModel();
        Model model = modelDefinition.getModel(ModelQuality.High);

        int i = 0;
        for (ffxiv.housim.saintcoinach.scene.mesh.Mesh m : model.getMeshes()) {
            i++;
            // mesh
            Mesh mesh = build(m);

            // material
            Material material = MaterialFactory.build(m.getMaterial());

            Geometry geom = new Geometry("#" + models + ":" + i);
            geom.setMesh(mesh);
            geom.setMaterial(material);
            geom.setShadowMode(RenderQueue.ShadowMode.Cast);
            thisNode.attachChild(geom);
        }

        root.attachChild(thisNode);
    }

    private static Mesh build(ffxiv.housim.saintcoinach.scene.mesh.Mesh m) {
        VertexFormat vertexFormat = m.getVertexFormat();

        short[] indices = m.getIndices();
        Vertex[] vertices = m.getVertices();
        int vertCount = m.getVertices().length;

        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Index, 1, indices);

        for (VertexFormatElement element : vertexFormat.getElements()) {
            switch (element.attribute) {
                case Position -> {
                    float[] positions = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].position;
                        positions[i * 4] = v.x;
                        positions[i * 4 + 1] = v.y;
                        positions[i * 4 + 2] = v.z;
                        positions[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Position, 4, positions);
                }
                case BoneWeights -> {
                    float[] weights = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].boneWeights;
                        weights[i * 4] = v.x;
                        weights[i * 4 + 1] = v.y;
                        weights[i * 4 + 2] = v.z;
                        weights[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.BoneWeight, 4, weights);
                }
                case BoneIndices -> {
                    short[] boneIndices = new short[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Ubyte4 v = vertices[i].boneIndices;
                        boneIndices[i * 4] = v.x;
                        boneIndices[i * 4 + 1] = v.y;
                        boneIndices[i * 4 + 2] = v.z;
                        boneIndices[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.BoneIndex, 4, boneIndices);
                }
                case Normal -> {
                    float[] normal = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].normal;
                        normal[i * 4] = v.x;
                        normal[i * 4 + 1] = v.y;
                        normal[i * 4 + 2] = v.z;
                        normal[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Normal, 4, normal);
                }
                case TexCoord -> {
                    float[] uv = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].texCoord;
                        uv[i * 4] = v.x;
                        uv[i * 4 + 1] = v.y;
                        uv[i * 4 + 2] = v.z;
                        uv[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.TexCoord, 4, uv);
                }
                case Binormal -> {
                    float[] binormal = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].binormal;
                        binormal[i * 4] = v.x;
                        binormal[i * 4 + 1] = v.y;
                        binormal[i * 4 + 2] = v.z;
                        binormal[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Binormal, 4, binormal);
                }
                case Tangent -> {
                    float[] tangent = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].tangent;
                        tangent[i * 4] = v.x;
                        tangent[i * 4 + 1] = v.y;
                        tangent[i * 4 + 2] = v.z;
                        tangent[i * 4 + 3] = v.w;
                    }
                    // use uv2 as Tangent2
                    mesh.setBuffer(VertexBuffer.Type.Tangent, 4, tangent);
                }
                case Color -> {
                    float[] color = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].color;
                        color[i * 4] = v.x;
                        color[i * 4 + 1] = v.y;
                        color[i * 4 + 2] = v.z;
                        color[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Color, 4, color);
                }
                default -> throw new IllegalArgumentException();
            }
        }

        mesh.setStatic();
        mesh.updateBound();
        mesh.updateCounts();

        return mesh;
    }

    public static Node load(Territory territory) {
        if (territory == null) {
            return null;
        }
        Node root = new Node(territory.getName());

        build(root, territory.getTerrain());

        return root;
    }

    public static void build(Node root, Terrain terrain) {

        if (terrain == null) {
            return;
        }
        TransformedModel[] models = terrain.getParts();

        for (int i=0; i<models.length; i++) {
            build(root, models[i], i);
        }
    }
}
