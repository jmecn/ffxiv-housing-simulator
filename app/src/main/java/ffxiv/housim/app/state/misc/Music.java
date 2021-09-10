package ffxiv.housim.app.state.misc;

import com.jme3.asset.AssetManager;
import com.jme3.audio.*;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import ffxiv.housim.app.plugins.SqpackRegister;
import ffxiv.housim.app.plugins.loader.ScdAudioData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Music {

    // final static String NAME = "music/ffxiv/bgm_con_bahamut_bigboss0.scd";
    // final static String NAME = "music/ffxiv/bgm_system_title.scd";
    // final static String NAME = "music/ffxiv/bgm_field_housing_night.scd";
    // final static String NAME = "music/ffxiv/bgm_field_housing_day.scd";
    //final static String NAME = "bgcommon/sound/hou/hou_spot_bell.scd";
    final static String NAME = "bgcommon/sound/hou/hou_spot_GriD_Yashiki_Cello.scd";

    public static void main(String[] args) {
        String gameDir = System.getenv("FFXIV_HOME");

        // Init AssetManager
        AssetManager assetManager = JmeSystem.newAssetManager();
        SqpackRegister.register(assetManager, gameDir);

        // Init AudioRenderer
        AudioRenderer audioRenderer = JmeSystem.newAudioRenderer(new AppSettings(true));
        audioRenderer.initialize();
        AudioContext.setAudioRenderer(audioRenderer);

        Float loopStartSec = null;
        Float loopEndSec = null;

        // Load SCD
        AudioKey audioKey = new AudioKey(NAME, false, false);

        ScdAudioData data = (ScdAudioData) assetManager.loadAudio(audioKey);
        loopStartSec = data.getLoopStartSec();
        loopEndSec = data.getLoopEndSec();
        AudioData audioData = data.getData();

        log.info("AudioData, type:{}, duration:{}, channels:{}, bps:{}, rate:{}", audioData.getDataType(), audioData.getDuration(), audioData.getChannels(), audioData.getBitsPerSample(), audioData.getSampleRate());

        AudioNode audioNode = new AudioNode(audioData, audioKey);
        audioNode.setPositional(false);
        audioNode.play();
        log.info("start time offset:{}", audioNode.getTimeOffset());

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioRenderer.update(0.1f);// AudioRenderer Updater

            if (loopEndSec != null && loopStartSec != null) {
                if (audioNode.getStatus() == AudioSource.Status.Stopped || audioNode.getPlaybackTime() >= loopEndSec) {
                    log.info("loop start:{}", loopStartSec);
                    audioNode.play();
                    audioNode.setTimeOffset(loopStartSec);
                }
            } else {
                if (audioNode.getStatus() == AudioSource.Status.Stopped) {
                    break;
                }
            }
        }
    }
}