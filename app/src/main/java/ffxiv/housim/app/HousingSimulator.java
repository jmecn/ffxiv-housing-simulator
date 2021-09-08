package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import ffxiv.housim.app.state.SplashState;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.io.PackCollection;

/**
 * desc: 主界面
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class HousingSimulator extends SimpleApplication {

    private PackCollection packs;
    private ARealmReversed ffxiv;

    public HousingSimulator(ARealmReversed ffxiv) {
        this.ffxiv = ffxiv;
        this.packs = ffxiv.getGameData().getPackCollection();
    }

    @Override
    public void simpleInitApp() {

        String gameDir = settings.getString(Constants.GAME_DIR);
        // init state
        stateManager.attach(new SplashState(ffxiv));

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
