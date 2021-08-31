package ffxiv.housim.app.state;

import com.jme3.app.*;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.asset.AssetInfo;
import com.jme3.audio.*;
import com.jme3.audio.plugins.OGGLoader;
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
import ffxiv.housim.saintcoinach.db.xiv.entity.bgm.BGM;
import ffxiv.housim.saintcoinach.db.xiv.entity.bgm.BGMSituation;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.XivMap;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.scene.terrain.Territory;
import ffxiv.housim.saintcoinach.sound.ScdEntry;
import ffxiv.housim.saintcoinach.sound.ScdEntryHeader;
import ffxiv.housim.saintcoinach.sound.ScdFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class HouseViewer extends SimpleApplication {

    private ARealmReversed ffxiv;
    private PackCollection packs;
    private List<TerritoryType> list;

    private int index;

    public HouseViewer() {
        super(new StatsAppState(), new FlyCamAppState(), new AudioListenerState(), new DebugKeysAppState(),
                new ConstantVerifierState(), new DetailedProfilerState());
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

        for (TerritoryType f : sheet) {
            if (f.getKey() == 0) {
                continue;
            }
            if (f.getBg() == null || f.getBg().isBlank()) {
                continue;
            }
            String name = f.getPlaceName().getName();
            if (name.contains("私人") || name.contains("个人")) {
                log.info("id:{}, name:{}, terr:{} > {} > {}", f.getKey(), f.getName(), f.getRegionPlaceName(), f.getPlaceName(), f.getZonePlaceName());
                list.add(f);
            }
        }

        list.sort((o1, o2) -> {
            String name1 = o1.getRegionPlaceName().getName();
            String name2 = o2.getRegionPlaceName().getName();

            String name3 = o1.getPlaceName().getName();
            String name4 = o2.getPlaceName().getName();

            int key1 = o1.getKey();
            int key2 = o2.getKey();
            if (name1.equals(name2)) {
                if (name3.equals(name4)) {
                    return key1 - key2;
                } else {
                    return name3.compareTo(name4);
                }
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

            log.info("load finished #{}, {} > {} > {} in {}ms.", f.getKey(), f.getRegionPlaceName(), f.getPlaceName(), f.getZonePlaceName(), ms);
            enqueue(() -> {
                viewNode.detachAllChildren();
                if (node != null) {
                    viewNode.attachChild(node);
                }
            });


            BGMSituation situation = f.getBGM();
            if (situation != null && situation.getDaytime() != null) {
                BGM bgm = situation.getDaytime();
                String name = bgm.getFile().toLowerCase();
                PackFile packFile = ffxiv.getGameData().getPackCollection().tryGetFile(name);
                if (packFile == null) {
                    log.warn("File not found:{}", name);
                    return;
                }

                ScdFile scdFile = new ScdFile(packFile);
                ScdEntry[] entries = scdFile.getEntries();
                if (entries == null || entries.length < 1) {
                    log.warn("Scd Entry is empty:{}", name);
                    return;
                }

                ScdEntryHeader header = scdFile.getEntryHeaders()[0];
                ScdEntry entry = entries[0];
                log.info("size:{}, sampleOffset:{}, loopStartSample:{}, loopEndSample:{}", header.dataSize, header.samplesOffset, header.loopStartSample, header.loopEndSample);

                AudioKey audioKey = new AudioKey(name, true, true);
                OGGLoader loader = new OGGLoader();
                AudioData audioData;
                try {
                    audioData = (AudioData) loader.load(new AssetInfo(null, audioKey) {
                        @Override
                        public InputStream openStream() {
                            return new ByteArrayInputStream(entry.getDecoded());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("Load OGG failed. {}", name, e);
                    return;
                }

                log.info("AudioData, type:{}, duration:{}, channels:{}, bps:{}, rate:{}", audioData.getDataType(), audioData.getDuration(), audioData.getChannels(), audioData.getBitsPerSample(), audioData.getSampleRate());

                enqueue(() -> {
                    if (audioNode.getStatus() == AudioSource.Status.Playing)  {
                        audioNode.stop();
                        audioNode.removeFromParent();
                    }
                    audioNode = new AudioNode(audioData, audioKey);
                    rootNode.attachChild(audioNode);
                    audioNode.setPositional(false);
                    audioNode.setLooping(true);
                    audioNode.play();
                });
            }

        }).start();

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
        app.start();
    }
}
