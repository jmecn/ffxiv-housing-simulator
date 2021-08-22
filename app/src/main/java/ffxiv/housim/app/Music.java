package ffxiv.housim.app;

import com.jme3.asset.AssetInfo;
import com.jme3.audio.*;
import com.jme3.audio.plugins.OGGLoader;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.sound.ScdEntry;
import ffxiv.housim.saintcoinach.sound.ScdEntryHeader;
import ffxiv.housim.saintcoinach.sound.ScdFile;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Music {

    // final static String NAME = "music/ffxiv/bgm_con_bahamut_bigboss0.scd";
    final static String NAME = "music/ffxiv/bgm_system_title.scd";

    public static void main(String[] args) {
        String gameDir = System.getenv("FFXIV_HOME");

        PackCollection coll = new PackCollection(gameDir + "/game/sqpack");
        PackFile packFile = coll.tryGetFile(NAME);
        if (packFile == null) {
            log.warn("File not found:{}", NAME);
            return;
        }

        ScdFile scdFile = new ScdFile(packFile);
        ScdEntry[] entries = scdFile.getEntries();
        if (entries == null || entries.length < 1) {
            log.warn("Scd Entry is empty:{}", NAME);
            return;
        }

        // Init AudioRenderer
        AudioRenderer audioRenderer = JmeSystem.newAudioRenderer(new AppSettings(true));
        audioRenderer.initialize();
        AudioContext.setAudioRenderer(audioRenderer);

        ScdEntryHeader header = scdFile.getEntryHeaders()[0];
        ScdEntry entry = entries[0];
        log.info("size:{}, sampleOffset:{}, loopStartSample:{}, loopEndSample:{}", header.dataSize, header.samplesOffset, header.loopStartSample, header.loopEndSample);

        AudioKey audioKey = new AudioKey(NAME, true, true);
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
            log.error("Load OGG failed. {}", NAME, e);
            return;
        }

        log.info("AudioData, type:{}, duration:{}, channels:{}, bps:{}, rate:{}", audioData.getDataType(), audioData.getDuration(), audioData.getChannels(), audioData.getBitsPerSample(), audioData.getSampleRate());

        AudioNode audioNode = new AudioNode(audioData, audioKey);
        audioNode.setPositional(false);
        audioNode.play();

        while (audioNode.getStatus() == AudioSource.Status.Playing) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioRenderer.update(0.1f);// AudioRenderer Updater
        }
    }
}
