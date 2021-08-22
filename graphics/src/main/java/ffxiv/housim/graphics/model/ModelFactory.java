package ffxiv.housim.graphics.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import ffxiv.housim.saintcoinach.graphics.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.graphics.model.*;
import ffxiv.housim.saintcoinach.graphics.sgb.*;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector4;
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
        int targets = 0;
        for (ISgbGroupEntry e : data.getEntries()) {

            if (e instanceof SgbGroupEntryModel me) {
                build(root, me, models++);
            } else if (e instanceof SgbGroupEntryChairMarker ce) {
                build(root, ce);
            } else if (e instanceof SgbGroupEntryTargetMarker te) {
                build(root, te, targets++);
            }
        }

        return root;
    }

    private static void build(Node root, SgbGroupEntryTargetMarker tc, int targets) {

        ColorRGBA color = new ColorRGBA(0f, 1f, 0f, 1f);

        Geometry mark = new Geometry("TargetMarker#" + targets);
        mark.setMesh(new Sphere(8, 4, 0.1f));
        mark.setMaterial(colorMaterial(color));

        // transform
        Vector3 trans = tc.getTranslation();
        Vector3 scale = tc.getScale();
        Vector3 rotate = tc.getRotation();

        mark.setLocalTranslation(trans.x, trans.y, trans.z);
        mark.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        mark.setLocalScale(scale.x, scale.y, scale.z);

        root.attachChild(mark);
    }

    private static void build(Node root, SgbGroupEntryChairMarker ce) {
        ColorRGBA color = new ColorRGBA(1.0f, 0f, 0f, 1f);

        Geometry mark = new Geometry("ChairMarker:" + ce.getName());
        mark.setMesh(new Sphere(8, 4, 0.1f));
        mark.setMaterial(colorMaterial(color));

        // transform
        Vector3 trans = ce.getTranslation();
        Vector3 scale = ce.getScale();
        Vector3 rotate = ce.getRotation();

        mark.setLocalTranslation(trans.x, trans.y, trans.z);
        mark.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        mark.setLocalScale(scale.x, scale.y, scale.z);

        root.attachChild(mark);
    }

    private static void build(Node root, SgbGroupEntryModel me, int models) {

        TransformedModel transformedModel = me.getModel();

        // transform
        Vector3 trans = transformedModel.getTranslation();
        Vector3 rotate = transformedModel.getRotation();
        Vector3 scale = transformedModel.getScale();
        log.info("trans:{}, rotate:{}, scale:{}", trans, rotate, scale);

        root.setLocalTranslation(trans.x, trans.y, trans.z);
        root.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        root.setLocalScale(scale.x, scale.y, scale.z);

        // model
        ModelDefinition modelDefinition = transformedModel.getModel();

        Model model = modelDefinition.getModel(ModelQuality.High);

        for (ffxiv.housim.saintcoinach.graphics.mesh.Mesh m : model.getMeshes()) {
            // mesh
            Mesh mesh = build(m);

            // material
            Material material = build(m.getMaterial());

            Geometry geom = new Geometry("#" + models);
            geom.setMesh(mesh);
            geom.setMaterial(material);
            root.attachChild(geom);
        }
    }

    private static Mesh build(ffxiv.housim.saintcoinach.graphics.mesh.Mesh m) {
        VertexFormat vertexFormat = m.getVertexFormat();

        short[] indices = m.getIndices();
        Vertex[] vertices = m.getVertices();
        int vertCount = m.getVertices().length;

        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Index, 1, indices);

        for (VertexFormatElement element : vertexFormat.getElements()) {
            switch (element.attribute) {
                case BlendIndices:
                    int[] boneIndices = new int[vertCount];
                    for ( int i = 0; i < vertCount; i++) {
                        boneIndices[i] = vertices[i].blendIndices;
                    }
                    mesh.setBuffer(VertexBuffer.Type.BoneIndex, 1, boneIndices);
                    break;
                case BlendWeights:{
                    float[] weights = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].blendWeights;
                        weights[i * 4] = v.x;
                        weights[i * 4 + 1] = v.y;
                        weights[i * 4 + 2] = v.z;
                        weights[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.BoneWeight, 4, weights);
                    break;
                }
                case Color: {
                    float[] color = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].color;
                        color[i * 4] = v.x;
                        color[i * 4 + 1] = v.y;
                        color[i * 4 + 2] = v.z;
                        color[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Color, 4, color);
                    break;
                }
                case Normal:
                    float[] normal = new float[vertCount * 3];
                    for ( int i = 0; i < vertCount; i++) {
                        Vector3 v = vertices[i].normal;
                        normal[i * 3] = v.x;
                        normal[i * 3 + 1] = v.y;
                        normal[i * 3 + 2] = v.z;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Normal, 3, normal);
                    break;
                case Position:
                    float[] positions = new float[vertCount * 3];
                    for ( int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].position;
                        positions[i * 3] = v.x;
                        positions[i * 3 + 1] = v.y;
                        positions[i * 3 + 2] = v.z;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Position, 3, positions);
                    break;
                case Tangent2: {
                    float[] tangent = new float[vertCount * 4];
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].tangent2;
                        tangent[i * 4] = v.x;
                        tangent[i * 4 + 1] = v.y;
                        tangent[i * 4 + 2] = v.z;
                        tangent[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Binormal, 4, tangent);
                    break;
                }
                case Tangent1: {
                    float[] tangent = new float[vertCount * 4];
                    for ( int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].tangent1;
                        tangent[i * 4] = v.x;
                        tangent[i * 4 + 1] = v.y;
                        tangent[i * 4 + 2] = v.z;
                        tangent[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Tangent, 4, tangent);
                    break;
                }
                case UV:
                    float[] uv = new float[vertCount * 2];
                    for ( int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].uv;
                        uv[i * 2] = v.x;
                        uv[i * 2 + 1] = v.y;
                    }
                    mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, uv);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        mesh.setStatic();
        mesh.updateBound();
        mesh.updateCounts();

        return mesh;
    }

    private static Material build(MaterialDefinition matDef) {
        Material mat = new Material(assetManager, Materials.LIGHTING);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setBoolean("UseMaterialColors", true);
        return mat;
    }

    private static Material colorMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setWireframe(true);
        return mat;
    }
}
