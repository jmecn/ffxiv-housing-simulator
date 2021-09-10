package ffxiv.housim.app.plugins.loader;

import com.google.common.io.Files;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.audio.AudioBuffer;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.audio.plugins.CachedOggStream;
import com.jme3.util.BufferUtils;
import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.vorbis.CommentHeader;
import de.jarnbjo.vorbis.IdentificationHeader;
import de.jarnbjo.vorbis.VorbisStream;
import ffxiv.housim.app.plugins.SqpackAssetInfo;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.sound.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;

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

            if (entry instanceof ScdOggEntry ogg) {
                audioData = loadOGG(ogg, audioKey);
            } else if (entry instanceof ScdAdpcmEntry wav) {
                AudioData data = loadWAV(wav, audioKey);
                if (data != null) {
                    audioData = new ScdAudioData(data);
                }
            }

            if (audioData != null) {
                break;
            }
        }

        return audioData;
    }

    private ScdAudioData loadOGG(ScdOggEntry entry, AudioKey audioKey) {
        byte[] decoded = entry.getDecoded();
        try {
            File file = new File("data/" + audioKey.getName() + ".ogg");
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            Files.write(decoded, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream bi = new ByteArrayInputStream(decoded);
        try {
            CachedOggStream oggStream = new CachedOggStream(bi);
            LogicalOggStream loStream = oggStream.getLogicalStreams().iterator().next();
            VorbisStream vorbisStream = new VorbisStream(loStream);

            IdentificationHeader streamHdr = vorbisStream.getIdentificationHeader();

            int channels = streamHdr.getChannels();
            int sampleRate = streamHdr.getSampleRate();
            int numSamples = (int) oggStream.getLastOggPage().getAbsoluteGranulePosition();

            // Number of Samples * Number of Channels * Bytes Per Sample
            int totalBytes = numSamples * channels * 2;

            log.info("Sample Rate: {}", sampleRate);
            log.info("Channels: {}", channels);
            log.info("Stream Length: {}", numSamples);
            log.info("Duration: {}", numSamples / (float) sampleRate);
            log.info("Bytes Calculated: {}", totalBytes);

            ByteBuffer buffer = readToBuffer(vorbisStream, totalBytes);

            // Get loop parameters
            CommentHeader commentHdr = vorbisStream.getCommentHeader();
            String loopStart = commentHdr.getComment("LoopStart");
            String loopEnd = commentHdr.getComment("LoopEnd");

            Integer loopStartSample = null;
            Integer loopEndSample = null;
            if (loopStart != null) {
                loopStartSample = Integer.parseInt(loopStart);
                log.info("LoopStart sample: {}", loopStartSample);
            }
            if (loopEnd != null) {
                loopEndSample = Integer.parseInt(loopEnd);
                log.info("LoopEnd sample: {}", loopEndSample);
            }

            AudioBuffer audioBuffer = new AudioBuffer();
            audioBuffer.setupFormat(channels, 16, sampleRate);
            audioBuffer.updateData(buffer);

            oggStream.close();
            loStream.close();
            vorbisStream.close();

            return new ScdAudioData(audioBuffer, loopStartSample, loopEndSample);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load OGG failed. {}", audioKey.getName(), e);
            return null;
        }

    }

    private ByteBuffer readToBuffer(VorbisStream vorbisStream, int totalBytes) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buf = new byte[512];
        int read = 0;

        try {
            while ( (read = vorbisStream.readPcm(buf, 0, buf.length)) > 0){
                baos.write(buf, 0, read);
            }
        } catch (EndOfOggStreamException ex){
        }

        byte[] dataBytes = baos.toByteArray();
        swapBytes(dataBytes, 0, dataBytes.length);

        log.info("Bytes Available:  {}", dataBytes.length);

        // Take the minimum of the number of bytes available
        // and the expected duration of the audio.
        int bytesToCopy =  Math.min(totalBytes, dataBytes.length );

        ByteBuffer data = BufferUtils.createByteBuffer(bytesToCopy);
        data.put(dataBytes, 0, bytesToCopy).flip();

        return data;
    }

    private static void swapBytes(byte[] b, int off, int len) {
        byte tempByte;
        for (int i = off; i < (off+len); i+=2) {
            tempByte = b[i];
            b[i] = b[i+1];
            b[i+1] = tempByte;
        }
    }

    private AudioData loadWAV(ScdAdpcmEntry entry, AudioKey audioKey) {

        byte[] decoded = entry.getDecoded();
        try {
            File file = new File("data/" + audioKey.getName() + ".wav");
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            Files.write(decoded, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        WavLoader loader = new WavLoader();

        AudioData audioData = null;
        try {
            audioData = (AudioData) loader.load(new AssetInfo(null, audioKey) {
                @Override
                public InputStream openStream() {
                    return new ByteArrayInputStream(decoded);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load WAV failed. {}", audioKey.getName(), e);
        }

        return audioData;
    }
}
