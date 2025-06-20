package ffxiv.housim.app.state.misc;

import com.google.common.collect.Sets;
import com.jme3.app.*;
import com.jme3.asset.AssetKey;
import com.jme3.audio.*;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.util.TempVars;
import ffxiv.housim.app.factory.MaterialFactory;
import ffxiv.housim.app.factory.ModelFactory;
import ffxiv.housim.app.state.CheckerBoardState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.scene.terrain.Territory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class HouseViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<TerritoryType> list;

    private int index;

    public HouseViewer() {
        super(new StatsAppState(), new FlyCamAppState());
    }

    private void initMap()  {
        String gameDir = System.getenv("FFXIV_HOME");
        try {
            ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        packs = ffxiv.getGameData().getPackCollection();
        IXivSheet<TerritoryType> sheet = ffxiv.getGameData().getSheet(TerritoryType.class);

        list = new ArrayList<>(sheet.getCount());

        Set<String> map = Sets.newHashSet("s1i1", "s1i2", "s1i3", "s1i4",
                "f1i1", "f1i2", "f1i3", "f1i4",
                "w1i1", "w1i2", "w1i3", "w1i4",
                "e1i1", "e1i2", "e1i3", "e1i4");

        for (TerritoryType f : sheet) {
            if (f.getKey() == 0) {
                continue;
            }
            if (f.getBg() == null || f.getBg().isBlank()) {
                continue;
            }
            String name = f.getName();
            if (map.contains(name)) {
                log.info("id:{}, name:{}, terr:{} > {} > {}", f.getKey(), f.getName(), f.getRegionPlaceName(), f.getPlaceName(), f.getZonePlaceName());
                list.add(f);
            }
        }

        list.sort((o1, o2) -> {
            String name1 = o1.getName();
            String name2 = o2.getName();

            int key1 = o1.getKey();
            int key2 = o2.getKey();
            if (name1.equals(name2)) {
                return key1 - key2;
            } else {
                return name1.compareTo(name2);
            }
        });
        index = 0;
    }

    private Node viewNode = new Node("view");
    private AudioNode audioNode = new AudioNode();

    @Override
    public void simpleInitApp() {
        initScene();

        initMap();

        initInput();

        ModelFactory.setPacks(packs);
        ModelFactory.setXiv(ffxiv.getGameData());
        ModelFactory.setAssetManager(assetManager);
        MaterialFactory.setPacks(packs);
        MaterialFactory.setAssetManager(assetManager);

        rootNode.attachChild(viewNode);
        rootNode.attachChild(audioNode);

        index = 0;
        reload();
    }

    private void initInput() {
        inputManager.addMapping("F_LEFT", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("F_RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT));
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
                    }
                }
            }
        }, "F_LEFT", "F_RIGHT", "F_PICK");
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

            TerritoryType f = list.get(index);

            log.info("loading finished #{}, {} > {} > {}.", f.getKey(), f.getRegionPlaceName(), f.getPlaceName(), f.getZonePlaceName());

            long ms = System.currentTimeMillis();
            Node node = ModelFactory.load(new Territory(f));
            ms = System.currentTimeMillis() - ms;

            int fId = FastMath.nextRandomInt(1, 45);
            int wId = FastMath.nextRandomInt(1, 45);
            String fl_mtrl = String.format("bgcommon/hou/dyna/mat/fl/%04d/material/rom_fl_2%04da.mtrl", fId, fId);
            String wl_mtrl = String.format("bgcommon/hou/dyna/mat/wl/%04d/material/rom_wl_2%04da.mtrl", wId, wId);

            Material fl_mat = MaterialFactory.build(fl_mtrl);
            Material wl_mat = MaterialFactory.build(wl_mtrl);
            String name = f.getName();

            log.info("load finished #{}, {} > {} > {} in {}ms.", f.getKey(), f.getRegionPlaceName(), f.getPlaceName(), f.getZonePlaceName(), ms);
            enqueue(() -> {
                viewNode.detachAllChildren();
                if (node != null) {
                    viewNode.attachChild(node);

                    Geometry fl_base;
                    Geometry fl_1st;
                    Geometry fl_2nd;
                    Geometry wl_base;
                    Geometry wl_1st;
                    Geometry wl_2nd;

                    if (name.endsWith("1")) {
                        fl_base = load(name, "fl", "base");
                        wl_base = load(name, "wl", "base");
                        fl_base.move(2, -7, 4);
                        wl_base.move(2, -7, 4);

                        viewNode.attachChild(fl_base);
                        viewNode.attachChild(wl_base);

                        fl_1st = load(name, "fl", "1st");
                        wl_1st = load(name, "wl", "1st");
                        viewNode.attachChild(fl_1st);
                        viewNode.attachChild(wl_1st);
                    }

                    if (name.endsWith("2")) {

                        fl_base = load(name, "fl", "base");
                        wl_base = load(name, "wl", "base");
                        fl_base.move(0, -7, 0);
                        wl_base.move(0, -7, 0);

                        viewNode.attachChild(fl_base);
                        viewNode.attachChild(wl_base);

                        fl_1st = load(name, "fl", "1st");
                        wl_1st = load(name, "wl", "1st");
                        viewNode.attachChild(fl_1st);
                        viewNode.attachChild(wl_1st);

                        fl_2nd = load(name,"fl", "2nd");
                        wl_2nd = load(name,"wl", "2nd");

                        fl_2nd.move(0, 7, 0);
                        wl_2nd.move(0, 7, 0);
                        viewNode.attachChild(fl_2nd);
                        viewNode.attachChild(wl_2nd);
                    }
                    if (name.endsWith("3")) {

                        fl_base = load(name, "fl", "base");
                        wl_base = load(name, "wl", "base");
                        fl_base.move(0, -7, 0);
                        wl_base.move(0, -7, 0);

                        viewNode.attachChild(fl_base);
                        viewNode.attachChild(wl_base);

                        fl_1st = load(name, "fl", "1st");
                        wl_1st = load(name, "wl", "1st");
                        viewNode.attachChild(fl_1st);
                        viewNode.attachChild(wl_1st);

                        fl_2nd = load(name,"fl", "2nd");
                        wl_2nd = load(name,"wl", "2nd");

                        fl_2nd.move(0, 7, 0);
                        wl_2nd.move(0, 7, 0);
                        viewNode.attachChild(fl_2nd);
                        viewNode.attachChild(wl_2nd);
                    }

                    if (name.endsWith("4")) {
                        fl_1st = (Geometry) assetManager.loadModel("Model/House/w1i4_fl_1st.j3o");
                        fl_1st.setMaterial(fl_mat);
                        wl_1st = (Geometry) assetManager.loadModel("Model/House/w1i4_wl_1st.j3o");
                        wl_1st.setMaterial(wl_mat);

                        load(name, "fl", "1st");
                        load(name, "wl", "1st");

                        viewNode.attachChild(fl_1st);
                        viewNode.attachChild(wl_1st);
                    }
                }
            });


        }).start();

    }

    private Geometry load(String place, String part, String floor) {
        String fl = String.format("Model/House/%s_%s_%s.j3o", place, part, floor);
        if (assetManager.locateAsset(new AssetKey<>(fl)) == null) {
            // 默认用高脚孤丘的
            if (place.endsWith("1")) {
                place = "w1i1";
            }
            if (place.endsWith("2")) {
                place = "w1i2";
            }
            if (place.endsWith("3")) {
                place = "w1i3";
            }
            if (place.endsWith("4")) {
                place = "w1i4";
            }
            fl = String.format("Model/House/%s_%s_%s.j3o", place, part, floor);
        }

        Geometry geom = (Geometry) assetManager.loadModel(fl);

        if (part.equals("fl")) {
            int fId = FastMath.nextRandomInt(1, 45);
            String fl_mtrl = String.format("bgcommon/hou/dyna/mat/fl/%04d/material/rom_fl_2%04da.mtrl", fId, fId);
            Material fl_mat = MaterialFactory.build(fl_mtrl);
            geom.setMaterial(fl_mat);

        } else if (part.equals("wl")) {
            int wId = FastMath.nextRandomInt(1, 45);
            String wl_mtrl = String.format("bgcommon/hou/dyna/mat/wl/%04d/material/rom_wl_2%04da.mtrl", wId, wId);
            Material wl_mat = MaterialFactory.build(wl_mtrl);
            geom.setMaterial(wl_mat);
        } else {
            geom.setMaterial(MaterialFactory.colorMaterial(ColorRGBA.Black));
        }

        return geom;
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

        flyCam.setMoveSpeed(20);
        flyCam.setDragToRotate(true);
    }

    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("Final Fantasy XIV House Viewer");
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        setting.setGammaCorrection(false);
        // LWJGL-OpenGL2
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);

        HouseViewer app = new HouseViewer();
        app.setSettings(setting);
        app.setShowSettings(false);
        app.start();
    }
}
