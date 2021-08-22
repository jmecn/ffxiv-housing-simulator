package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.graphics.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.graphics.model.*;
import ffxiv.housim.saintcoinach.graphics.sgb.*;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector4;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.entity.housing.HousingFurniture;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FurnitureViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<HousingFurniture> furnitures;

    private int index;

    public FurnitureViewer() {
        super();
    }

    private void initFurnitures()  {
        String gameDir = System.getenv("FFXIV_HOME");
        try {
            ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        packs = ffxiv.getGameData().getPackCollection();
        IXivSheet<HousingFurniture> sheet = ffxiv.getGameData().getSheet(HousingFurniture.class);

        furnitures = new ArrayList<>(sheet.getCount());

        for (HousingFurniture f : sheet) {
            if (f.getSgbPath() == null || f.getSgbPath().isBlank()) {
                log.info("ignore HousingFurniture #{}, {}", f.getModelKey(), f.getItem());
                continue;
            }
            if (f.getItem() == null || f.getItem().getName().isBlank()) {
                log.info("ignore HousingFurniture #{}, {}", f.getModelKey(), f.getSgbPath());
                continue;
            }
            furnitures.add(f);
        }

        index = 0;
    }

    private Node furnutureNode = new Node("furnuture");

    @Override
    public void simpleInitApp() {
        initScene();

        initFurnitures();

        initInput();

        rootNode.attachChild(furnutureNode);

        reload();
    }

    private void initInput() {
        inputManager.addMapping("F_LEFT", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("F_RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {

                if (isPressed) {
                    switch (name) {
                        case "F_RIGHT": {
                            index++;
                            if (index >= furnitures.size()) {
                                index = 0;
                            }
                            reload();
                            break;
                        }
                        case "F_LEFT": {
                            index--;
                            if (index < 0) {
                                index = furnitures.size() - 1;
                            }
                            reload();
                            break;
                        }
                    }
                }
            }
        }, "F_LEFT", "F_RIGHT");
    }

    private void reload() {
        enqueue(() -> {
            HousingFurniture f = furnitures.get(index);
            log.info("load #{}, {}, {}", f.getModelKey(), f.getItem(), f.getSgbPath());
            Node node = load(f.getSgbPath());
            furnutureNode.detachAllChildren();
            furnutureNode.attachChild(node);
        });
    }

    private Node load(String sgbPath) {

        PackFile file = packs.tryGetFile(sgbPath);
        SgbFile sgbFile = new SgbFile(file);

        Node root = new Node(sgbFile.getFile().getPath());
        SgbGroup data = (SgbGroup) sgbFile.getData()[0];
        for (ISgbGroupEntry e : data.getEntries()) {

            if (e instanceof SgbGroupEntryModel me) {
                build(root, me);
            } else if (e instanceof SgbGroupEntryChairMarker ce) {
                build(root, ce);
            } else if (e instanceof SgbGroupEntryTargetMarker te) {
                build(root, te);
            }
        }

        return root;
    }

    private void build(Node root, SgbGroupEntryTargetMarker tc) {

        ColorRGBA color = new ColorRGBA(0f, 1f, 0f, 1f);

        Geometry mark = new Geometry("TargetMarker:" + tc.getName());
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

    private void build(Node root, SgbGroupEntryChairMarker ce) {
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

    private void build(Node root, SgbGroupEntryModel me) {

        TransformedModel transformedModel = me.getModel();

        // transform
        Vector3 trans = transformedModel.getTranslation();
        Vector3 rotate = transformedModel.getRotation();
        Vector3 scale = transformedModel.getScale();

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

            Geometry geom = new Geometry();
            geom.setMesh(mesh);
            geom.setMaterial(material);
            root.attachChild(geom);
        }

        rootNode.attachChild(root);
    }

    private Mesh build(ffxiv.housim.saintcoinach.graphics.mesh.Mesh m) {
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

    private Material build(MaterialDefinition matDef) {
        Material mat = new Material(assetManager, Materials.LIGHTING);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setBoolean("UseMaterialColors", true);
        return mat;
    }

    private Material colorMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setWireframe(true);
        return mat;
    }

    private void initScene() {
        //viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);

        rootNode.addLight(new AmbientLight(new ColorRGBA(0.3f, 0.3f, 0.3f, 1f)));

        rootNode.addLight(new DirectionalLight(
                new Vector3f(-3, -4, -5).normalizeLocal(),
                new ColorRGBA(0.7f, 0.7f, 0.7f, 1f)));
    }

    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("Final Fantasy XIV Housing Furniture Viewer");
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        // LWJGL-OpenGL2
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);

        FurnitureViewer app = new FurnitureViewer();
        app.setSettings(setting);
        app.start();
    }
}
