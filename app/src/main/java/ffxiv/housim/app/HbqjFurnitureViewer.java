package ffxiv.housim.app;

import com.google.gson.*;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.util.TempVars;
import ffxiv.housim.graphics.model.ModelFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.state.LightState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.graphics.model.Model;
import ffxiv.housim.saintcoinach.io.Index;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.entity.housing.HousingFurniture;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HbqjFurnitureViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<HousingFurniture> list;

    private Map<String, HousingFurniture> map;

    private int index;

    public HbqjFurnitureViewer() {
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

        list = new ArrayList<>(sheet.getCount());
        map = new HashMap<>();

        for (HousingFurniture f : sheet) {
            if (f.getSgbPath() == null || f.getSgbPath().isBlank()) {
                log.info("ignore HousingFurniture #{}, {}", f.getModelKey(), f.getItem());
                continue;
            }
            if (f.getItem() == null || f.getItem().getName().isBlank()) {
                log.info("ignore HousingFurniture #{}, {}", f.getModelKey(), f.getSgbPath());
                continue;
            }
            list.add(f);
            map.put(f.getItem().getName(), f);
        }

        index = 0;
    }

    private Node viewNode = new Node("furnuture");

    @Override
    public void simpleInitApp() {
        stateManager.attach(new CheckerBoardState());
        initScene();

        initFurnitures();

        initInput();

        ModelFactory.setPacks(packs);
        ModelFactory.setAssetManager(assetManager);

        rootNode.attachChild(viewNode);

        new Thread(() -> {
            reload();
        }).start();

    }

    private void initInput() {
        inputManager.addMapping("F_PICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {

                if (isPressed) {
                    switch (name) {
                        case "F_PICK": {
                            pick();
                        }
                    }
                }
            }
        }, "F_PICK");
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

    private Map<Integer, String> initHbqjFur() {
        Map<Integer, String> map = new HashMap<>();
        File file = new File("fur");
        if (!file.exists()) {
            log.warn("File not exist");
            return map;
        }
        String data;
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();;
            return map;
        }
        JsonObject json = new JsonParser().parse(data).getAsJsonObject();
        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            String name = e.getKey();
            int id = e.getValue().getAsInt();
            log.info("id:{}, name:{}", id, name);
            map.put(id, name);
        }
        return map;
    }
    private void reload() {
        Map<Integer, String> fur = initHbqjFur();

        File file = new File("和风茶舍S.json");
        if (!file.exists()) {
            log.warn("File not exist");
            return;
        }
        String data;
        try {
            data = Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();;
            return;
        }
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(data);

        JsonArray list;
        if (root instanceof JsonObject obj) {
            int size = obj.getAsJsonPrimitive("size").getAsInt();
            log.info("size:{}", size);
            if (size <= 0) {
                return;
            }
            list = obj.getAsJsonArray("list");
        } else {
            return;
        }

        for (JsonElement e : list) {
            JsonObject v = e.getAsJsonObject();
            int id = v.get("categoryId").getAsInt();
            int count = v.get("count").getAsInt();

            String name = fur.get(id);

            HousingFurniture f = map.get(name);
            log.info("id:{}, count:{}, f:{}", id, count, f);

            JsonArray posX = v.get("posX").getAsJsonArray();
            JsonArray posY = v.get("posY").getAsJsonArray();
            JsonArray posZ =  v.get("posZ").getAsJsonArray();
            JsonArray rot = v.get("Rotation").getAsJsonArray();


            for (int i = 0; i < count; i++) {
                float x = (float)posX.get(i).getAsDouble();
                float y = (float)posY.get(i).getAsDouble();
                float z = (float)posZ.get(i).getAsDouble();
                float r = (float)rot.get(i).getAsDouble();

                Node node = ModelFactory.load(f.getSgbPath());
                node.setLocalTranslation(x, y, z);
                node.setLocalRotation(new Quaternion().fromAngleAxis(r, Vector3f.UNIT_Y));

                log.info("#{}, {}, ({},{},{}), r={}", id, f, x, y, z, r);
                enqueue(() -> {
                    viewNode.attachChild(node);
                });
            }
        }
    }

    private void initScene() {
        stateManager.attach(new CheckerBoardState());
        stateManager.attach(new LightState());

        Spatial sky = SkyFactory.createSky(assetManager, "sky/inside.hdr", SkyFactory.EnvMapType.EquirectMap);
        rootNode.attachChild(sky);

        cam.setLocation(new Vector3f(0f, 5f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
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

        HbqjFurnitureViewer app = new HbqjFurnitureViewer();
        app.setSettings(setting);
        app.start();
    }
}
