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
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import ffxiv.housim.app.plugins.SqpackRegister;
import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.graphics.factory.MaterialFactory;
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.factory.TextureFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.state.LightState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.io.Pack;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackDirectory;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;
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
public class SplashState extends BaseAppState implements SceneProcessor {

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

    private Node splash;

    private Geometry bg;
    private Geometry loading;
    private int w;
    private int h;
    private int padding = 40;

    int rx = 400;
    int ry = 168;

    private Material tileMaterial;

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

    float time = 0f;

    @Override
    public void update(float tpf) {
        // 控制光标的闪烁
        time += tpf;
        if (time > 1.0f) {
            time -= 1.0f;
        }

        tileMaterial.setFloat("Time", time);

        if (done.get()) {
            BgmState state = stateManager.getState(BgmState.class);
            if (state != null && state.isInitialized()) {
                onDisable();
                log.info("done!");
            }
        }
    }

    private void initSplash() {
        w = cam.getWidth();
        h = cam.getHeight();

        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", ColorRGBA.Black);

        bg = new Geometry("bg");
        bg.setMesh(new Quad(w + padding * 2, h + padding * 2));
        bg.setMaterial(mat);

        splash = new Node("splash");
        splash.move(0, 0, 100);

        splash.attachChild(bg);
        bg.move(-padding, -padding, -1);

        initLoading();

        guiNode.attachChild(splash);
    }

    private void initLoading() {
        String path = "common/graphics/texture";
        Pack pack = packs.tryGetPack(path);
        if (pack == null) {
            return;
        }
        PackDirectory dir = pack.getSource().tryGetDirectory(path);
        if (dir == null) {
            return;
        }

        PackFile file = dir.tryGetFile(0x271D5C69);
        if (file instanceof ImageFile image) {
            image.setPath(path + "/0x271D5C69");
            Texture2D tex = TextureFactory.get(image);

            tileMaterial = new Material(assetManager, "MatDefs/Tile/Tile.j3md");
            tileMaterial.setTexture("ColorMap", tex);
            tileMaterial.setVector2("Params", new Vector2f(4, 4));
            tileMaterial.setFloat("Speed", 2.f);
            tileMaterial.setFloat("Time", 0.0f);
            tileMaterial.setFloat("AlphaDiscardThreshold", 0.5f);

            // resolution
            loading = new Geometry("loading");
            loading.setMesh(new Quad(rx, ry, true));
            loading.setMaterial(tileMaterial);

            splash.attachChild(loading);
            loading.move((w - rx) / 2, (h - ry) / 2, 0);
        }
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

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
        if (w > this.w || h > this.h) {
            float sx = (float)w / this.w;
            float sy = (float)h / this.h;
            float scale = Math.max(sx, sy);
            bg.setLocalScale(scale);
        }

        loading.move((w - rx) / 2, (h - ry) / 2, 0);
    }

    @Override
    public void preFrame(float tpf) {
    }

    @Override
    public void postQueue(RenderQueue rq) {
    }

    @Override
    public void postFrame(FrameBuffer out) {
    }

    @Override
    public void setProfiler(AppProfiler profiler) {
    }
}
