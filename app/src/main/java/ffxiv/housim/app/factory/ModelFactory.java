package ffxiv.housim.app.factory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.*;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Sphere;
import com.simsilica.lemur.dnd.DragEvent;
import com.simsilica.lemur.event.*;
import ffxiv.housim.app.plugins.loader.ScdAudioData;
import ffxiv.housim.app.state.BgmState;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.math.Ubyte4;
import ffxiv.housim.saintcoinach.scene.lgb.*;
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

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ModelFactory {

    @Setter
    static PackCollection packs;

    @Setter
    static XivCollection xiv;

    @Setter
    static AssetManager assetManager;
    @Setter
    static AppStateManager stateManager;

    static Cache<Integer, Mesh> CACHE;
    static {
        CACHE = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(3600))
                .softValues()
                .build();
    }

    public static Node load(String path) {
        if (path.endsWith("sgb")) {
            return loadSgb(path);
        } else if (path.endsWith("lgb")) {
            return loadLgb(path);
        } else {
            return null;
        }
    }

    public static Node loadLgb(String lgbPath) {
        PackFile file = packs.tryGetFile(lgbPath);
        if (file != null) {
            log.debug("load:{}", lgbPath);

            LgbFile lgbFile = new LgbFile(file);
            Node root = new Node(file.getPath());
            build(root, lgbFile);

            return root;
        }
        log.warn("lgb file not found:{}", lgbPath);
        return null;
    }

    public static Node loadSgb(String sgbPath) {
        log.debug("load:{}", sgbPath);

        PackFile file = packs.tryGetFile(sgbPath);
        if (file != null) {
            SgbFile sgbFile = new SgbFile(file);
            Node root = new Node(file.getPath());

            build(root, sgbFile);

            return root;
        }

        return null;
    }

    private static void build(Node root, SgbFile sgbFile) {
        if (sgbFile == null) {
            return;
        }

        for (ISgbData group : sgbFile.getData()) {
            SgbGroup data = (SgbGroup) group;

            String name = data.getName();
            log.debug("build sgb:{}", name);

            int models = 0;
            int chairs = 0;
            int targets = 0;
            for (ISgbEntry e : data.getEntries()) {

                if (e == null) {
                    continue;
                }

                if (e instanceof SgbEntryModel) {
                    SgbEntryModel me = (SgbEntryModel) e;
                    build(root, me, models++);
                } else if (e instanceof SgbEntryChairMarker) {
                    SgbEntryChairMarker ce = (SgbEntryChairMarker) e;
                    build(root, ce, chairs++);
                } else if (e instanceof SgbEntryTargetMarker) {
                    SgbEntryTargetMarker te = (SgbEntryTargetMarker) e;
                    build(root, te, targets++);
                } else if (e instanceof SgbEntryGimmick) {
                    SgbEntryGimmick te = (SgbEntryGimmick) e;
                    build(root, te, targets++);
                } else if (e instanceof SgbEntry1C) {
                    SgbEntry1C te = (SgbEntry1C) e;
                    build(root, te, targets++);
                } else if (e instanceof SgbEntrySound) {
                    SgbEntrySound scd = (SgbEntrySound) e;
                    build(root, scd);
                } else {
                    log.warn("unsupported entry:{}", e);
                }
            }
        }
    }

    private static void build(Node root, SgbEntrySound scd) {
        AudioKey key = new AudioKey(scd.getScdFilePath(), false, false);

        AudioData audioData = assetManager.loadAudio(key);
        if (audioData instanceof ScdAudioData) {
            ScdAudioData scdAudioData = (ScdAudioData) audioData;
            AudioNode node = new AudioNode(scdAudioData.getData(), key);
            if (audioData.getChannels() == 1) {
                node.setPositional(true);
                Vector3 trans = scd.getTranslation();
                node.move(trans.x, trans.y, trans.z);
            }
            root.attachChild(node);
            log.info("add sound:{}", scd.getScdFilePath());
        }
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

    static Material chairMat = null;
    private static void build(Node root, SgbEntryChairMarker ce, int chairs) {
        ColorRGBA color = new ColorRGBA(1.0f, 0f, 0f, 1f);
        if (chairMat == null) {
            chairMat = MaterialFactory.colorMaterial(color);
        }

        Spatial spatial = assetManager.loadModel("Model/Chair/scene.gltf");
        spatial.scale(0.25f);

        spatial.depthFirstTraversal(s -> {
            if (s instanceof Geometry) {
                Geometry e = (Geometry) s;
                log.info("name:{}, e:{}", e.getName(), e);
                e.setMaterial(chairMat);
            }
        });

        Node mark = new Node("ChairMarker#" + chairs);
        mark.attachChild(spatial);

        // transform
        Vector3 trans = ce.getTranslation();
        Vector3 scale = ce.getScale();
        Vector3 rotate = ce.getRotation();

        mark.setLocalTranslation(trans.x, trans.y, trans.z);
        mark.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        //mark.setLocalScale(scale.x, scale.y, scale.z);

        root.attachChild(mark);
    }

    private static void build(Node root, SgbEntryModel me, int models) {
        build(root, me.getModel(), models);
    }
    private static void build(Node root, SgbEntryGimmick e, int models) {
        SgbFile gimmick = e.getGimmick();

        build(root, gimmick);
    }

    private static void build(Node root, SgbEntry1C e, int models) {
        SgbFile gimmick = e.getGimmick();
        Model model = e.getModel();

        if (gimmick != null) {
            build(root, gimmick);
        }

        if (model != null) {
            build(root, model, models);
        }
    }
    private static void build(Node root, TransformedModel transformedModel, int index) {
        if (transformedModel == null) {
            return;
        }

        Node thisNode = new Node();
        // transform
        Vector3 trans = transformedModel.getTranslation();
        Vector3 rotate = transformedModel.getRotation();
        Vector3 scale = transformedModel.getScale();
        log.debug("trans:{}, rotate:{}, scale:{}", trans, rotate, scale);

        thisNode.setLocalTranslation(trans.x, trans.y, trans.z);
        thisNode.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        thisNode.setLocalScale(scale.x, scale.y, scale.z);

        root.attachChild(thisNode);

        // model
        ModelDefinition modelDefinition = transformedModel.getModel();
        Model model = modelDefinition.getModel(ModelQuality.High);
        thisNode.setName(model.getName() + "#" + index);

        log.debug("load mdl:{}", modelDefinition.getFile().getPath());
        build(thisNode, model, index);
    }


    private static void build(Node root, Model model, int index) {

        int i = 0;
            for (ffxiv.housim.saintcoinach.scene.mesh.Mesh m : model.getMeshes()) {
            i++;

            // mesh
            Mesh mesh;
            try {
                mesh = CACHE.get(m.getHash(), () -> build(m));
            } catch (ExecutionException e) {
                mesh = build(m);
            }

            // material
            Material material = MaterialFactory.build(m.getMaterial());

            Geometry geom = new Geometry(model.getName() + "#" + index + ":" + i);
            geom.setMesh(mesh);
            geom.setMaterial(material);
            geom.setShadowMode(RenderQueue.ShadowMode.Cast);
            root.attachChild(geom);
        }

    }
    private static Mesh build(ffxiv.housim.saintcoinach.scene.mesh.Mesh m) {
        VertexFormat vertexFormat = m.getVertexFormat();

        short[] indices = m.getIndices();
        Vertex[] vertices = m.getVertices();
        int vertCount = vertices.length;

        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Index, 1, indices);

        float[] positions = new float[vertCount * 4];
        float[] boneWeights = new float[vertCount * 4];
        short[] boneIndices = new short[vertCount * 4];
        float[] normal = new float[vertCount * 4];
        float[] uv = new float[vertCount * 4];
        float[] binormal = new float[vertCount * 4];
        float[] tangent = new float[vertCount * 4];
        float[] color = new float[vertCount * 4];

        for (VertexFormatElement element : vertexFormat.getElements()) {
            switch (element.attribute) {
                case Position : {
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].position;
                        positions[i * 4] = v.x;
                        positions[i * 4 + 1] = v.y;
                        positions[i * 4 + 2] = v.z;
                        positions[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Position, 4, positions);
                    break;
                }
                case BoneWeights : {
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].boneWeights;
                        boneWeights[i * 4] = v.x;
                        boneWeights[i * 4 + 1] = v.y;
                        boneWeights[i * 4 + 2] = v.z;
                        boneWeights[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.BoneWeight, 4, boneWeights);
                    break;
                }
                case BoneIndices : {
                    for (int i = 0; i < vertCount; i++) {
                        Ubyte4 v = vertices[i].boneIndices;
                        boneIndices[i * 4] = v.x;
                        boneIndices[i * 4 + 1] = v.y;
                        boneIndices[i * 4 + 2] = v.z;
                        boneIndices[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.BoneIndex, 4, boneIndices);
                    break;
                }
                case Normal : {
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].normal;
                        normal[i * 4] = v.x;
                        normal[i * 4 + 1] = v.y;
                        normal[i * 4 + 2] = v.z;
                        normal[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Normal, 4, normal);
                    break;
                }
                case TexCoord : {
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].texCoord;
                        uv[i * 4] = v.x;
                        uv[i * 4 + 1] = v.y;
                        uv[i * 4 + 2] = v.z;
                        uv[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.TexCoord, 4, uv);
                    break;
                }
                case Binormal : {
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].binormal;
                        binormal[i * 4] = v.x;
                        binormal[i * 4 + 1] = v.y;
                        binormal[i * 4 + 2] = v.z;
                        binormal[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Binormal, 4, binormal);
                    break;
                }
                case Tangent : {
                    for (int i = 0; i < vertCount; i++) {
                        Vector4 v = vertices[i].tangent;
                        tangent[i * 4] = v.x;
                        tangent[i * 4 + 1] = v.y;
                        tangent[i * 4 + 2] = v.z;
                        tangent[i * 4 + 3] = v.w;
                    }
                    mesh.setBuffer(VertexBuffer.Type.Tangent, 4, tangent);
                    break;
                }
                case Color : {
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
                default : {
                    throw new IllegalArgumentException();
                }
            }
        }

        /**
        VertexBuffer tb = mesh.getBuffer(VertexBuffer.Type.Tangent);
        VertexBuffer nb = mesh.getBuffer(VertexBuffer.Type.Normal);
        VertexBuffer bnb = mesh.getBuffer(VertexBuffer.Type.Binormal);
        if (tb == null && nb != null && bnb != null) {
            TempVars vars = TempVars.get();

            Vector3f t = vars.vect1;
            Vector3f n = vars.vect2;
            Vector3f bn = vars.vect3;
            Vector3f none = vars.vect4;
            for (int i = 0; i < vertCount; i+=4) {
                n.set(normal[i], normal[i+1], normal[i+2]);
                n.normalizeLocal();

                bn.set(binormal[i], binormal[i+1], binormal[i+2]);
                bn.multLocal(2.0f).subtractLocal(Vector3f.UNIT_XYZ);
                float w = binormal[i+3] * 2f - 1f;

                bn.multLocal(w);
                bn.normalizeLocal();

                float dot = bn.dot(n);
                if (Math.abs(dot) > 0.01f) {
                    log.warn("bn dot n:{}", dot);
                }
                bn.cross(n, t);
                t.normalizeLocal();
                tangent[i] = t.x;
                tangent[i+1] = t.y;
                tangent[i+2] = t.z;
                tangent[i+3] = 1.0f;
            }

            mesh.setBuffer(VertexBuffer.Type.Tangent, 4, tangent);
            vars.release();
        }
        */

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

        // 基础的地形
        build(root, territory.getTerrain());

        // 植被、树木之类的
        build(root, territory.getBg());

        // 市场牌子
        build(root, territory.getPlanmap());

        // 活动相关内容
        build(root, territory.getPlanevent());

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

    public static void build(Node root, LgbFile lgb) {
        for (LgbGroup data : lgb.getGroups()) {
            String name = data.getName();

            if (name.startsWith("copy_")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.startsWith("QST")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2016_00")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2017_00")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2018_00")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2019_00")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2020_00")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2021_00")) {
                log.info("ignore {}", name);
                continue;
            }
            if (name.endsWith("2022_00")) {
                log.info("ignore {}", name);
                continue;
            }
            log.info("build lgb group:{}", name);
            int models = 0;
            for (ILgbEntry e : data.getEntries()) {
                if (e == null) {
                    continue;
                }
                if (e instanceof LgbEntryModel) {
                    LgbEntryModel me = (LgbEntryModel) e;
                    build(root, me, models++);
                } else if (e instanceof LgbEntryGimmick) {
                    LgbEntryGimmick g = (LgbEntryGimmick) e;
                    build(root, g, models++);
                } else if (e instanceof LgbEntryEObj) {
                    LgbEntryEObj eo = (LgbEntryEObj) e;
                    build(root, eo, models++);
                } else {
                    log.warn("unsupported entry:{}", e);
                }
            }
        }
    }


    private static void build(Node root, LgbEntryModel e, int models) {

        build(root, e.getModel(), models);

    }


    private static void build(Node root, LgbEntryGimmick e, int models) {
        SgbFile sgbFile = e.getGimmick();
        Node thisNode = new Node("lgbGimmick#" + e.getName());

        // transform
        Vector3 trans = e.translation;
        Vector3 rotate = e.rotation;
        Vector3 scale = e.scale;

        thisNode.setLocalTranslation(trans.x, trans.y, trans.z);
        thisNode.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        thisNode.setLocalScale(scale.x, scale.y, scale.z);

        if (sgbFile != null) {
            build(thisNode, sgbFile);
        }
        root.attachChild(thisNode);
    }


    private static void build(Node root, LgbEntryEObj e, int models) {
        Node thisNode = new Node("lgbModel#" + e.getName());

        // transform
        Vector3 trans = e.translation;
        Vector3 rotate = e.rotation;
        Vector3 scale = e.scale;

        thisNode.setLocalTranslation(trans.x, trans.y, trans.z);
        thisNode.setLocalRotation(new Quaternion().fromAngles(rotate.x, rotate.y, rotate.z));
        thisNode.setLocalScale(scale.x, scale.y, scale.z);

        root.attachChild(thisNode);

        SgbFile gimmick = e.getGimmick(xiv);

        build(thisNode, gimmick);

    }
}
