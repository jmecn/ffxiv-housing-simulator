package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import ffxiv.housim.app.state.MainMenu;
import ffxiv.housim.graphics.factory.MaterialFactory;
import ffxiv.housim.graphics.factory.ModelFactory;
import ffxiv.housim.graphics.state.CheckerBoardState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.io.PackCollection;

import java.io.IOException;

/**
 * desc: 主界面
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class App extends SimpleApplication {

    private PackCollection packs;
    private ARealmReversed ffxiv;

    @Override
    public void simpleInitApp() {
        // init game dir
        String gameDir = settings.getString(Constants.GAME_DIR);
        try {
            ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        packs = ffxiv.getGameData().getPackCollection();

        // init factories
        ModelFactory.setPacks(packs);
        ModelFactory.setAssetManager(assetManager);

        MaterialFactory.setPacks(packs);
        MaterialFactory.setAssetManager(assetManager);

        // init sky
        Spatial sky = SkyFactory.createSky(assetManager, "sky/env1.hdr", SkyFactory.EnvMapType.EquirectMap);
        rootNode.attachChild(sky);

        // init lemur
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

        // init state
        stateManager.attach(new CheckerBoardState());
        stateManager.attach(new MainMenu());

        // init camera
        cam.setLocation(new Vector3f(0f, 3f, 10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFov(60);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
    }
}
