package ffxiv.housim.app.state.misc;

import com.jme3.app.SimpleApplication;
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
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.state.LightState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingExterior;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingInterior;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingItemCategory;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingSize;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExteriorViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<HousingExterior> list;

    private Map<Integer, Item> hi2i = new HashMap<>();
    private Map<Integer, Item> he2i = new HashMap<>();

    private int index;

    public ExteriorViewer() {
        super();
    }

    private void initItems()  {
        String gameDir = System.getenv("FFXIV_HOME");
        try {
            ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        packs = ffxiv.getGameData().getPackCollection();

        IXivSheet<Item> items = ffxiv.getGameData().getSheet(Item.class);

        for (Item i : items) {
            if (14 != i.getFilterGroup()) {// 14 for housing
                continue;
            }

            IXivRow row = i.getAdditionalData();
            if (row instanceof HousingInterior) {
                hi2i.put(row.getKey(), i);
            } else if (row instanceof HousingExterior) {
                he2i.put(row.getKey(), i);
            }
        }

        IXivSheet<HousingExterior> sheet = ffxiv.getGameData().getSheet(HousingExterior.class);

        list = new ArrayList<>(sheet.getCount());

        for (HousingExterior f : sheet) {
            if (f.getHousingItemCategory() == 0) {
                log.info("ignore HousingExterior #{}", f.getExteriorId());
                continue;
            }
            Item item = he2i.get(f.getKey());
            int cat = f.getHousingItemCategory();
            if (f.getModel().isBlank() && item == null && (cat != 5 && cat != 6 && cat != 7)) {
                log.info("ignore HousingExterior #{}, category:{}, item:{}", f.getExteriorId(), f.getHousingItemCategory(), item);
                continue;
            }
            list.add(f);
        }

        index = 0;
    }

    private Node viewNode = new Node("Housing Exterior");

    @Override
    public void simpleInitApp() {
        initScene();

        initItems();

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
            try {
                HousingExterior f = list.get(index);

                Item item = he2i.get(f.getKey());
                HousingItemCategory hic = HousingItemCategory.of(f.getHousingItemCategory());
                HousingSize hs = HousingSize.of(f.getHousingSize());

                String model = f.getModel();
                if (f.getHousingItemCategory() == 5) {
                    model = String.format("bgcommon/hou/dyna/opt/rf/%04d/asset/opt_rf_m%04d.sgb", f.getExteriorId(), f.getExteriorId());
                } else if (f.getHousingItemCategory() == 6) {
                    model = String.format("bgcommon/hou/dyna/opt/wl/%04d/asset/opt_wl_m%04d.sgb", f.getExteriorId(), f.getExteriorId());
                } else if (f.getHousingItemCategory() == 7) {
                    model = String.format("bgcommon/hou/dyna/opt/sg/%04d/asset/opt_sg_m%04d.sgb", f.getExteriorId(), f.getExteriorId());
                }
                if (model.isBlank()) {
                    log.info("No model found");
                    return;
                }
                log.info("load #{}:{}, id:{}, cat:{}, size:{}", f.getKey(), item, f.getExteriorId(), hic.getName(), hs.getDesc());
                Node node = ModelFactory.load(model);
                viewNode.detachAllChildren();
                viewNode.attachChild(node);
            } catch (Exception e) {
                e.printStackTrace();;
            }
        });
    }

    private void initScene() {
        stateManager.attach(new CheckerBoardState());
        stateManager.attach(new LightState());

        Spatial sky = SkyFactory.createSky(assetManager, "sky/env1.hdr", SkyFactory.EnvMapType.EquirectMap);
        rootNode.attachChild(sky);

        cam.setLocation(new Vector3f(0f, 5f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
    }

    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("Final Fantasy XIV Housing Exterior Viewer");
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        setting.setGammaCorrection(false);
        // LWJGL-OpenGL2
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);

        ExteriorViewer app = new ExteriorViewer();
        app.setSettings(setting);
        app.start();
    }
}
