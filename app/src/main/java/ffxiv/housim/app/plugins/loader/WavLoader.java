package ffxiv.housim.app.plugins.loader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.audio.AudioBuffer;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.audio.AudioStream;
import com.jme3.audio.SeekableStream;
import com.jme3.util.BufferUtils;
import com.jme3.util.LittleEndien;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class WavLoader implements AssetLoader {

    // all these are in big endian
    private static final int i_RIFF = 0x46464952;
    private static final int i_WAVE = 0x45564157;
    private static final int i_fmt  = 0x20746D66;
    private static final int i_data = 0x61746164;

    // format tag
    private static final int WAVE_FORMAT_PCM       = 0x01;
    private static final int WAVE_FORMAT_MS_ADPCM  = 0x02;// ffxiv use MS-ADPCM codec for wav sound effect

    private boolean readStream = false;

    private AudioBuffer audioBuffer;
    private AudioStream audioStream;
    private AudioData audioData;

    // fmt chunk
    private int format; // format type
    private int channels;// number of channels
    private int sampleRate; // sample rate
    private int bytesPerSec;// for buffer estimation
    private int blockAlign; // block size of data
    private int bitsPerSample;// number of bits per sample of mono data
    private int cbSize;// the count in bytes of the extra information (after cbSize)
    private byte[] extra;

    private float duration;

    private ResettableInputStream in;
    private int inOffset = 0;
    
    private static class ResettableInputStream extends LittleEndien implements SeekableStream {
        
        final private AssetInfo info;
        private int resetOffset = 0;
        
        public ResettableInputStream(AssetInfo info, InputStream in) {
            super(in);
            this.info = info;
        }
        
        public void setResetOffset(int resetOffset) {
            this.resetOffset = resetOffset;
        }

        @Override
        public void setTime(float time) {
            if (time != 0f) {
                throw new UnsupportedOperationException("Seeking WAV files not supported");
            }
            InputStream newStream = info.openStream();
            try {
                newStream.skip(resetOffset);
                this.in = new BufferedInputStream(newStream);
            } catch (IOException ex) {
                // Resource could have gotten lost, etc.
                try {
                    newStream.close();
                } catch (IOException ex2) {
                }
                throw new RuntimeException(ex);
            }
        }
    }

    private void readFormatChunk(int size) throws IOException{
        format = in.readShort();
        channels = in.readShort();
        sampleRate = in.readInt();
        bytesPerSec = in.readInt(); // used to calculate duration
        blockAlign = in.readShort();
        bitsPerSample = in.readShort();

        int expectedBytesPerSec = (bitsPerSample * channels * sampleRate) / 8;
        if (expectedBytesPerSec != bytesPerSec){
            log.warn("Expected {} bytes per second, got {}", expectedBytesPerSec, bytesPerSec);
        }

        audioData.setupFormat(channels, bitsPerSample, sampleRate);

        int remaining = size - 16;
        if (remaining > 0){

            if (remaining >= 2) {
                cbSize = in.readShort();
                remaining -= 2;

                if (cbSize > 0) {
                    if (remaining < cbSize) {
                        throw new IOException("Invalid cbSize in fmt chunk");
                    }
                    extra = new byte[cbSize];
                    in.readFully(extra);

                    remaining -= cbSize;
                }
            }

            in.skipBytes(remaining);
        }
    }

    /* These are for MS-ADPCM */

    private static final short AdpcmAdaptationTable[] = {
            230, 230, 230, 230, 307, 409, 512, 614,
            768, 614, 512, 409, 307, 230, 230, 230,
    };
    private static final short AdpcmAdaptCoeff1[] = {
            256, 512, 0, 192, 240, 460, 392
    };
    private static final short AdpcmAdaptCoeff2[] = {
            0, -256, 0, 64, 0, -208, -232
    };

    private static final int[] SIGNED = {
            // Used to bias the prediction based on the previous encoded value.
            0,1,2,3,4,5,6,7,-8,-7,-6,-5,-4,-3,-2,-1
    };

    class AdpcmChannelStatus {
        int sample1;
        int sample2;
        int coeff1;
        int coeff2;
        int delta;
    }

    private void decodeMSADPCM(int len) throws IOException {
        if (readStream) {
            throw new IOException("Stream is not supported with ADPCM format WAV file.");
        }

        // extra size always 0x20
        if (extra == null) {
            throw new IOException("MS ADPCM extra expect extra data");
        }

        ByteBuffer buffer = ByteBuffer.wrap(extra);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // MsAdpcmExtra
        short samplesPerBlock = buffer.getShort();// always 128
        short numCoef = buffer.getShort();// always 7
        short[] array = new short[numCoef * 2];// always coef table
        for (int i = 0; i < numCoef; i++) {
            array[i] = buffer.getShort();
            array[i + numCoef] = buffer.getShort();
        }

        int st = channels == 2 ? 1 : 0;// Stereo or Mono

        int blockCount = len / blockAlign;
        int samples = blockCount * samplesPerBlock;

        log.info("data len:{}, blockAlign:{}, blockCount:{}, samplesPerBlock:{}, samples:{}, duration:{}",
                len, blockAlign, blockCount, samplesPerBlock, samples, duration);

        ByteBuffer data = BufferUtils.createByteBuffer(samples * 2 * channels)
                .order(ByteOrder.LITTLE_ENDIAN);
        log.info("buffer class:{}, order:{}", data.getClass(), data.order());

        AdpcmChannelStatus[] c = new AdpcmChannelStatus[channels];
        c[0] = new AdpcmChannelStatus();
        if (st != 0) c[1] = new AdpcmChannelStatus();

        byte[] block = new byte[blockAlign];

        for (int i = 0; i < blockCount; i++) {
            in.readFully(block);

            ByteBuffer gb = ByteBuffer.wrap(block);
            gb.order(ByteOrder.LITTLE_ENDIAN);

            int block_predictor = gb.get() & 0xFF;
            if (block_predictor > 6) {
                throw new IOException("Error: block predicator=" + block_predictor);
            }
            c[0].coeff1 = AdpcmAdaptCoeff1[block_predictor];
            c[0].coeff2 = AdpcmAdaptCoeff2[block_predictor];
            if (st != 0) {
                block_predictor = gb.get() & 0xFF;
                if (block_predictor > 6) {
                    throw new IOException("Error: block predicator=" + block_predictor);
                }
                c[1].coeff1 = AdpcmAdaptCoeff1[block_predictor];
                c[1].coeff2 = AdpcmAdaptCoeff2[block_predictor];
            }

            c[0].delta = gb.getShort();
            if (st != 0) c[1].delta = gb.getShort();
            c[0].sample1 = gb.getShort();
            if (st != 0) c[1].sample1 = gb.getShort();
            c[0].sample2 = gb.getShort();
            if (st != 0) c[1].sample2 = gb.getShort();

            data.putShort((short)c[0].sample2);
            if (st != 0) data.putShort((short)c[1].sample2);
            data.putShort((short)c[0].sample1);
            if (st != 0) data.putShort((short)c[1].sample1);

            for (int n = (samplesPerBlock - 2) >> (1 - st); n > 0; n--) {
                int s = gb.get() & 0xFF;
                int h = s >> 4;
                int l = s & 0x0F;
                data.putShort( adpcmMsExpandNibble(c[0], h) );
                data.putShort( adpcmMsExpandNibble(c[st], l) );
            }
        }

        data.flip();
        audioBuffer.setupFormat(channels, 16, sampleRate);
        audioBuffer.updateData(data);

        in.close();
    }

    private short adpcmMsExpandNibble(AdpcmChannelStatus c, int nibble) {
        int predictor = (c.sample1 * c.coeff1 + c.sample2 * c.coeff2) / 256;
        predictor += SIGNED[nibble] * c.delta;

        // clip to int16
        if (predictor > Short.MAX_VALUE) predictor = Short.MAX_VALUE;
        else if (predictor < Short.MIN_VALUE) predictor = Short.MIN_VALUE;

        c.sample2 = c.sample1;
        c.sample1 = (short)predictor;

        c.delta = (AdpcmAdaptationTable[nibble] * c.delta) >> 8;
        if (c.delta < 16) c.delta = 16;
        else if (c.delta > Short.MAX_VALUE) {
            log.warn("delta overflow:{}", c.delta);
            c.delta = Short.MAX_VALUE;
        }

        return (short) c.sample1;
    }

    private void decodePCM(int len) throws IOException {
        audioData.setupFormat(channels, bitsPerSample, sampleRate);
        duration = len / bytesPerSec;

        if (readStream) {
            readDataChunkForStream(inOffset, len);
        } else {
            readDataChunkForBuffer(len);
        }
    }

    private void readDataChunkForBuffer(int len) throws IOException {
        ByteBuffer data = BufferUtils.createByteBuffer(len);
        byte[] buf = new byte[512];
        int read = 0;
        while ( (read = in.read(buf)) > 0){
            data.put(buf, 0, Math.min(read, data.remaining()) );
        }
        data.flip();
        audioBuffer.updateData(data);
        in.close();
    }

    private void readDataChunkForStream(int offset, int len) throws IOException {
        in.setResetOffset(offset);
        audioStream.updateData(in, duration);
    }

    private AudioData load(AssetInfo info, InputStream inputStream, boolean stream) throws IOException{
        this.in = new ResettableInputStream(info, inputStream);
        inOffset = 0;
        
        int sig = in.readInt();
        if (sig != i_RIFF)
            throw new IOException("File is not a WAVE file");
        
        // skip size
        in.readInt();
        if (in.readInt() != i_WAVE)
            throw new IOException("WAVE File does not contain audio");

        inOffset += 4 * 3;
        
        readStream = stream;
        if (readStream){
            audioStream = new AudioStream();
            audioData = audioStream;
        }else{
            audioBuffer = new AudioBuffer();
            audioData = audioBuffer;
        }

        while (true) {
            int type = in.readInt();
            int len = in.readInt();
            
            inOffset += 4 * 2;

            switch (type) {
                case i_fmt:
                    readFormatChunk(len);
                    inOffset += len;
                    break;
                case i_data:
                    if (format == WAVE_FORMAT_PCM) {
                        decodePCM(len);
                        return audioData;
                    }
                    if (format == WAVE_FORMAT_MS_ADPCM) {
                        decodeMSADPCM(len);
                        return audioData;
                    }
                    break;
                default:
                    int skipped = in.skipBytes(len);
                    if (skipped <= 0) {
                        return null;
                    }
                    inOffset += skipped;
                    break;
            }
        }
    }
    
    @Override
    public Object load(AssetInfo info) throws IOException {
        AudioData data;
        InputStream inputStream = null;
        try {
            inputStream = info.openStream();
            data = load(info, inputStream, ((AudioKey)info.getKey()).isStream());
            if (data instanceof AudioStream){
                inputStream = null;
            }
            return data;
        } finally {
            if (inputStream != null){
                inputStream.close();
            }
        }
    }

    public static void main(String[] args) {
        int a = -29395;
        short b = (short) a;
        System.out.println(a + " " + b);
    }
}
