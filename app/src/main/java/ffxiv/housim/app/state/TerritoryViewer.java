package ffxiv.housim.app.state;

import com.jme3.app.*;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.audio.AudioListenerState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TempVars;
import ffxiv.housim.graphics.factory.MaterialFactory;
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.factory.TextureFactory;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.XivMap;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TerritoryViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<XivMap> list;

    private int index;

    public TerritoryViewer() {
        super(new StatsAppState(), new FlyCamAppState(), new AudioListenerState(), new DebugKeysAppState(),
                new ConstantVerifierState(), new DetailedProfilerState());
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
        IXivSheet<XivMap> sheet = ffxiv.getGameData().getSheet(XivMap.class);

        list = new ArrayList<>(sheet.getCount());

        for (XivMap f : sheet) {
            if (f.getKey() == 0) {
                continue;
            }
            if (f.getTerritoryType() == null || f.getTerritoryType().getKey() == 0) {
                continue;
            }
            list.add(f);
        }

        index = 0;
    }

    private Node viewNode = new Node("view");

    private Node mapNode = new Node("map");

    @Override
    public void simpleInitApp() {
        initScene();

        initFurnitures();

        initInput();

        ModelFactory.setPacks(packs);
        ModelFactory.setAssetManager(assetManager);
        MaterialFactory.setPacks(packs);
        MaterialFactory.setAssetManager(assetManager);

        rootNode.attachChild(viewNode);
        guiNode.attachChild(mapNode);

        index = 78;
        reload();
    }

    private void initInput() {
        inputManager.addMapping("F_LEFT", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("F_RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("F_MAP", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("F_PICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {

                if (isPressed) {
                    switch (name) {
                        case "F_RIGHT": {
                            index++;
                            if (index >= list.size()) {
                                index = 0;
                            }
                            reload();
                            break;
                        }
                        case "F_LEFT": {
                            index--;
                            if (index < 0) {
                                index = list.size() - 1;
                            }
                            reload();
                            break;
                        }
                        case "F_PICK": {
                            pick();
                            break;
                        }
                        case "F_MAP": {
                            if (mapNode.getParent() == guiNode) {
                                mapNode.removeFromParent();
                            } else {
                                guiNode.attachChild(mapNode);
                            }
                            break;
                        }
                    }
                }
            }
        }, "F_LEFT", "F_RIGHT", "F_PICK", "F_MAP");
    }

    private void pick() {

        TempVars vars = TempVars.get();

        Vector3f dir = cam.getWorldCoordinates(inputManager.getCursorPosition(), 1f, vars.vect1);
        dir.subtractLocal(cam.getLocation());
        Ray ray = new Ray(cam.getLocation(), dir);

        CollisionResults results = vars.collisionResults;
        results.clear();

        viewNode.collideWith(ray, results);

        CollisionResult result = results.getClosestCollision();
        if (result != null) {
            log.info("geom:{}", result.getGeometry().getName());
        }

        vars.release();
    }

    private void reload() {
        new Thread(() ->{
            XivMap f = list.get(index);
            log.info("load #{}, {} > {} > {}", f.getKey(), f.getRegionPlaceName(), f.getPlaceName(), f.getLocationPlaceName());
            Node node = ModelFactory.load(f.getTerritory());
            Geometry geom = getMeduimMap();

            log.info("finished.");
            enqueue(() -> {
                viewNode.detachAllChildren();
                if (node != null) {
                    viewNode.attachChild(node);
                    Vector3f center = node.getWorldBound().getCenter();
                    cam.setLocation(center);
                }
                mapNode.detachAllChildren();
                if (geom != null) {
                    mapNode.attachChild(geom);
                }
            });
        }).start();

    }

    private Geometry getMeduimMap() {
        XivMap f = list.get(index);
        log.info("load small map:{}", f.getId());
        ImageFile image = f.getSmallImage();
        ImageFile imageMask = f.getSmallImageMask();

        Texture2D map = null;
        Texture2D mask = null;
        if (image != null) {
            map = TextureFactory.get(image);
        }
        if (imageMask != null) {
            mask = TextureFactory.get(imageMask);
        }

        if (map == null) {
            log.info("map is null");
            return null;
        }

        Picture picture = new Picture(f.getId());
        picture.setTexture(assetManager, map, true);

        float scaleW = cam.getWidth() / (float) image.getWidth();
        float scaleH = cam.getHeight() / (float) image.getHeight();
        float scale = Math.min(scaleH, scaleW);
        picture.setWidth(image.getWidth() * scale);
        picture.setHeight(image.getHeight() * scale);

        return picture;
    }

    private void initScene() {
        stateManager.attach(new CheckerBoardState());

        viewPort.setBackgroundColor(new ColorRGBA(0.75f, 0.8f, 0.9f, 1f));

        // 环境光
        AmbientLight al = new AmbientLight(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        DirectionalLight dl = new DirectionalLight(
                new Vector3f(-4, -10, -5).normalizeLocal(),
                new ColorRGBA(0.7f, 0.7f, 0.7f, 0.7f));
        rootNode.addLight(al);
        viewNode.addLight(dl);

        Spatial sky = SkyFactory.createSky(assetManager, "sky/env1.hdr", SkyFactory.EnvMapType.EquirectMap);
        rootNode.attachChild(sky);

        cam.setLocation(new Vector3f(0f, 5f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFov(60);

        flyCam.setMoveSpeed(50);
        flyCam.setDragToRotate(true);
    }

    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("Final Fantasy XIV Terrain Viewer");
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        setting.setGammaCorrection(false);
        // LWJGL-OpenGL2
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);

        TerritoryViewer app = new TerritoryViewer();
        app.setSettings(setting);
        app.start();
    }
}
