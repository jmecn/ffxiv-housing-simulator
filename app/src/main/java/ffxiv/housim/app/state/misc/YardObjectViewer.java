package ffxiv.housim.app.state.misc;

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
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.util.TempVars;
import ffxiv.housim.graphics.factory.MaterialFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.state.LightState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingYardObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class YardObjectViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<HousingYardObject> list;

    private int index;

    public YardObjectViewer() {
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
        IXivSheet<HousingYardObject> sheet = ffxiv.getGameData().getSheet(HousingYardObject.class);

        list = new ArrayList<>(sheet.getCount());

        for (HousingYardObject f : sheet) {
            if (f.getSgbPath() == null || f.getSgbPath().isBlank()) {
                log.info("ignore HousingYardObject #{}, {}", f.getModelKey(), f.getItem());
                continue;
            }
            if (f.getItem() == null || f.getItem().getName().isBlank()) {
                log.info("ignore HousingYardObject #{}, {}", f.getModelKey(), f.getSgbPath());
                continue;
            }
            list.add(f);
        }

        index = 0;
    }

    private Node viewNode = new Node("yard_object");

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
        enqueue(() -> {
            HousingYardObject f = list.get(index);
            log.info("load #{}, {}, {}", f.getModelKey(), f.getItem(), f.getSgbPath());
            Node node = ModelFactory.load(f.getSgbPath());
            viewNode.detachAllChildren();
            viewNode.attachChild(node);
        });
    }

    private void initScene() {
        stateManager.attach(new CheckerBoardState());
        stateManager.attach(new LightState());

        Spatial sky = SkyFactory.createSky(assetManager, "sky/env1.hdr", SkyFactory.EnvMapType.EquirectMap);
        rootNode.attachChild(sky);

        cam.setLocation(new Vector3f(0f, 5f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFov(60);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
    }

    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("Final Fantasy XIV Housing Yard Object Viewer");
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        setting.setGammaCorrection(false);
        // LWJGL-OpenGL2
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);

        YardObjectViewer app = new YardObjectViewer();
        app.setSettings(setting);
        app.start();
    }
}
