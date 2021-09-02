package ffxiv.housim.app.plugins.loader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.audio.plugins.OGGLoader;
import com.jme3.audio.plugins.WAVLoader;
import ffxiv.housim.app.plugins.SqpackAssetInfo;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.sound.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/2
 */
@Slf4j
public class ScdLoader implements AssetLoader {

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {

        if (assetInfo instanceof SqpackAssetInfo pack) {
            return load(pack);
        }
        return null;
    }

    private ScdAudioData load(SqpackAssetInfo pack) {
        PackFile packFile = pack.getPackFile();

        String name = packFile.getPath();
        ScdFile scdFile = new ScdFile(packFile);
        ScdEntry[] entries = scdFile.getEntries();
        if (entries == null || entries.length < 1) {
            log.warn("Scd Entry is empty:{}", name);
            return null;
        }

        ScdAudioData audioData = null;

        AudioKey audioKey = null;
        if (pack.getKey() instanceof AudioKey key) {
            audioKey = key;
        } else {
            audioKey = new AudioKey(name, true, true);
        }

        for (ScdEntry entry : entries) {
            ScdEntryHeader header = entry.getHeader();
            log.info("codec:{}, size:{}, sampleOffset:{}, frequency:{}, loopStartSample:{}, loopEndSample:{}", header.codec, header.dataSize, header.samplesOffset, header.frequency, header.loopStartSample, header.loopEndSample);

            if (entry instanceof ScdOggEntry ogg) {
                AudioData data = loadOGG(ogg, audioKey);
                if (data != null) {
                    log.info("Bit/Sample:{}, SampleRate:{}, Duration:{}", data.getBitsPerSample(), data.getSampleRate(), data.getDuration());
                    audioData = new ScdAudioData(data, ogg);
                }
            } else if (entry instanceof ScdAdpcmEntry wav) {
                AudioData data = loadWAV(wav, audioKey);
                if (data != null) {
                    log.info("Bit/Sample:{}, SampleRate:{}, Duration:{}", data.getBitsPerSample(), data.getSampleRate(), data.getDuration());
                    audioData = new ScdAudioData(data, wav);
                }
            }

            if (audioData != null) {
                break;
            }
        }

        return audioData;
    }

    private AudioData loadOGG(ScdOggEntry entry, AudioKey audioKey) {

        OGGLoader loader = new OGGLoader();
        AudioData audioData = null;
        try {
            audioData = (AudioData) loader.load(new AssetInfo(null, audioKey) {
                @Override
                public InputStream openStream() {
                    return new ByteArrayInputStream(entry.getDecoded());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load OGG failed. {}", audioKey.getName(), e);
        }
        return audioData;
    }

    private AudioData loadWAV(ScdAdpcmEntry entry, AudioKey audioKey) {

        WAVLoader loader = new WAVLoader();

        AudioData audioData = null;
        try {
            audioData = (AudioData) loader.load(new AssetInfo(null, audioKey) {
                @Override
                public InputStream openStream() {
                    return new ByteArrayInputStream(entry.getDecoded());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load WAV failed. {}", audioKey.getName(), e);
        }

        return audioData;
    }
}
