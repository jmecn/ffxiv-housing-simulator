package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import ffxiv.housim.app.plugins.SqpackLoader;
import ffxiv.housim.app.plugins.SqpackLocator;
import ffxiv.housim.app.plugins.SqpackRegister;
import ffxiv.housim.app.state.BgmState;
import ffxiv.housim.app.state.IconState;
import ffxiv.housim.app.state.MainMenu;
import ffxiv.housim.graphics.factory.MaterialFactory;
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.graphics.state.LightState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.ui.lemur.window.SimpleWindowManager;

/**
 * desc: 主界面
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class App extends SimpleApplication {

    private PackCollection packs;
    private ARealmReversed ffxiv;

    public App(ARealmReversed ffxiv) {
        this.ffxiv = ffxiv;
        this.packs = ffxiv.getGameData().getPackCollection();
    }

    @Override
    public void simpleInitApp() {
        String gameDir = settings.getString(Constants.GAME_DIR);
        SqpackRegister.register(assetManager, gameDir);

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

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);
        GuiGlobals.getInstance().getStyles().setDefault(font);

        // init state
        stateManager.attach(new BgmState());
        stateManager.attach(new MainMenu());
        stateManager.attach(new CheckerBoardState());
        stateManager.attach(new LightState());
        stateManager.attach(new SimpleWindowManager());

        stateManager.attach(new IconState(packs));

        // init camera
        cam.setLocation(new Vector3f(0f, 3f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFov(60);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
    }

    int frame = 0;
    public void simpleUpdate(float tpf) {
        if (frame == 1) {
            // TODO
        }
        frame ++;
    }
}
