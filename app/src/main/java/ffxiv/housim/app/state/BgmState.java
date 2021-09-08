package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.audio.*;
import com.jme3.scene.Node;
import ffxiv.housim.app.plugins.loader.ScdAudioData;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author yanmaoyuan
 * @date 2021/9/2
 */
@Slf4j
public class BgmState extends BaseAppState {

    public final static String DAYTIME = "music/ffxiv/bgm_field_housing_day.scd";
    public final static String NIGHT = "music/ffxiv/bgm_field_housing_night.scd";

    private AssetManager assetManager;

    private Node rootNode;
    private AudioNode bgmNode;

    private Float loopStartSec = null;
    private Float loopEndSec = null;

    @Override
    protected void initialize(Application app) {
        assetManager = app.getAssetManager();
        if (app instanceof SimpleApplication simpleApp) {
            rootNode = simpleApp.getRootNode();
        }
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    public void play(String path) {

        AudioKey audioKey = new AudioKey(path, true, true);
        try {
            AudioData data = assetManager.loadAudio(audioKey);

            loopStartSec = null;
            loopEndSec = null;
            if (data instanceof ScdAudioData scd) {
                data = scd.getData();
                loopStartSec = scd.getLoopStartSec();
                loopEndSec = scd.getLoopEndSec();
            }

            if (data != null) {
                if (bgmNode != null) {
                    bgmNode.stop();
                    bgmNode.removeFromParent();
                }

                bgmNode = new AudioNode(data, audioKey);

                rootNode.attachChild(bgmNode);
                bgmNode.setPositional(false);
                bgmNode.setLooping(false);
                bgmNode.play();
            }
        } catch (AssetNotFoundException | AssetLoadException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(float tpf) {

        // check loop
        if (loopStartSec != null) {
            if (bgmNode.getStatus() == AudioSource.Status.Stopped ||
                    (loopEndSec != null && bgmNode.getPlaybackTime() >= loopEndSec)) {
                log.info("loop start:{}", loopStartSec);
                bgmNode.setTimeOffset(loopStartSec);
                bgmNode.play();
            }
        }
    }
}
