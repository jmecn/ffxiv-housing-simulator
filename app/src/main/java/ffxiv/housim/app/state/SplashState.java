package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import ffxiv.housim.app.plugins.SqpackRegister;
import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.graphics.factory.MaterialFactory;
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.state.LightState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.ui.lemur.window.SimpleWindowManager;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * desc:启动界面
 *
 * @author yanmaoyuan
 * @date 2021/9/5
 */
@Slf4j
public class SplashState extends BaseAppState {

    private ARealmReversed ffxiv;
    private PackCollection packs;

    private Application app;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private AssetManager assetManager;
    private Camera cam;
    private ViewPort viewPort;

    private Node guiNode;
    private Node rootNode;

    private Spatial splash;

    public SplashState(ARealmReversed ffxiv) {
        this.ffxiv = ffxiv;
        this.packs = ffxiv.getGameData().getPackCollection();
    }

    // thread signal
    private AtomicBoolean done = new AtomicBoolean(false);
    private Object lock = new Object();

    @Override
    protected void initialize(Application app) {
        this.app = app;
        this.inputManager = app.getInputManager();
        this.assetManager = app.getAssetManager();
        this.stateManager = app.getStateManager();
        this.cam = app.getCamera();
        this.viewPort = app.getViewPort();

        if (app instanceof SimpleApplication simpleApp) {
            this.rootNode = simpleApp.getRootNode();
            this.guiNode = simpleApp.getGuiNode();
        }

        StatsAppState stats = stateManager.getState(StatsAppState.class);
        if (stats != null) {
            stats.setDisplayFps(false);
            stats.setDisplayStatView(false);
        }

        initSplash();

        Thread thread = new Thread("ffxiv-housim-init") {
            public void run() {
                synchronized (lock) {
                    initialize();
                    done.set(true);
                    lock.notifyAll();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
        if (splash != null) {
            splash.removeFromParent();
        }
        stateManager.detach(this);
    }

    @Override
    public void update(float tpf) {
        if (done.get()) {
            onDisable();
            log.info("done!");
        }
    }

    private void initSplash() {
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", ColorRGBA.Black);
        Geometry bg = new Geometry("bg");
        bg.setMesh(new Quad(cam.getWidth(), cam.getHeight()));
        bg.setMaterial(mat);

        Node node = new Node("splash");
        node.attachChild(bg);
        node.move(0, 0, 100);

        splash = node;

        guiNode.attachChild(splash);
    }

    private void initialize() {

        // init database
        XivDatabase database = new XivDatabase(ffxiv);
        database.init();

        SqpackRegister.register(assetManager, ffxiv.getGameDirectory().getPath());

        // init factories
        ModelFactory.setPacks(packs);
        ModelFactory.setAssetManager(assetManager);

        MaterialFactory.setPacks(packs);
        MaterialFactory.setAssetManager(assetManager);

        // init sky
        Spatial sky = SkyFactory.createSky(assetManager, "sky/env1.hdr", SkyFactory.EnvMapType.EquirectMap);
        rootNode.attachChild(sky);

        // init lemur
        BitmapFont font = assetManager.loadFont("Font/indoor.fnt");

        GuiGlobals.initialize(app);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);
        GuiGlobals.getInstance().getStyles().setDefault(font);

        // init state
        stateManager.attach(new BgmState());
        stateManager.attach(new MainMenu());
        stateManager.attach(new CheckerBoardState());
        stateManager.attach(new LightState());
        stateManager.attach(new SimpleWindowManager());

        // stateManager.attach(new IconState(packs));

        // init camera
        cam.setLocation(new Vector3f(0f, 3f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFov(60);
    }
}
