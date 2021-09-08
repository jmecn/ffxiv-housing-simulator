package ffxiv.housim.app.state.misc;

import com.google.common.io.Files;
import com.jme3.asset.AssetInfo;
import com.jme3.audio.*;
import com.jme3.audio.plugins.CachedOggStream;
import com.jme3.audio.plugins.OGGLoader;
import com.jme3.audio.plugins.UncachedOggStream;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.vorbis.VorbisStream;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.sound.ScdEntry;
import ffxiv.housim.saintcoinach.sound.ScdEntryHeader;
import ffxiv.housim.saintcoinach.sound.ScdFile;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Music {

    // final static String NAME = "music/ffxiv/bgm_con_bahamut_bigboss0.scd";
    // final static String NAME = "music/ffxiv/bgm_system_title.scd";
    final static String NAME = "music/ffxiv/bgm_field_housing_night.scd";
    //final static String NAME = "music/ffxiv/bgm_field_housing_day.scd";

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
        log.info("size:{}, channel:{}, sampleOffset:{}, loopStartSample:{}, loopEndSample:{}", header.dataSize, header.channelCount, header.samplesOffset, header.loopStartSample, header.loopEndSample);

        Integer loopStartSample = null;
        Integer loopEndSample = null;
        Float loopStartSec = null;
        Float loopEndSec = null;
        ByteArrayInputStream bi = new ByteArrayInputStream(entry.getDecoded());
        try {
            UncachedOggStream oggStream = new UncachedOggStream(bi);
            LogicalOggStream loStream = oggStream.getLogicalStreams().iterator().next();
            VorbisStream vorbisStream = new VorbisStream(loStream);
            int sampleRate = vorbisStream.getIdentificationHeader().getSampleRate();

            String loopStart = vorbisStream.getCommentHeader().getComment("LoopStart");
            if (loopStart != null) {
                loopStartSample = Integer.parseInt(loopStart);
                loopStartSec = loopStartSample / (float) sampleRate;
                log.info("loopStart sample:{}, sec:{}", loopStartSample, loopStartSec);
            }

            String loopEnd = vorbisStream.getCommentHeader().getComment("LoopEnd");
            if (loopEnd != null) {
                loopEndSample = Integer.parseInt(loopEnd);
                loopEndSec = loopEndSample / (float) sampleRate;
                log.info("loopEnd sample:{}, sec:{}", loopEndSample, loopEndSec);
            }

            oggStream.close();
            loStream.close();
            vorbisStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AudioKey audioKey = new AudioKey(NAME, false, false);
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

        try {
            Files.write(entry.getDecoded(), new File("data/tmp.ogg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            log.info("status:{}, time:{}, offset:{}", audioNode.getStatus(), audioNode.getPlaybackTime(), audioNode.getTimeOffset());
            if (loopEndSec != null && loopStartSec != null) {
                if (audioNode.getStatus() == AudioSource.Status.Stopped || audioNode.getPlaybackTime() >= loopEndSec) {
                    log.info("loop start:{}", loopStartSec);// CommentHeader -> Comments -> LoopStart -> LoopTime = LoopStart / SampleRate
                    audioNode.play();
                    audioNode.setTimeOffset(loopStartSec);
                }
            }
        }
    }
}