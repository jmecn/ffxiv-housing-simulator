package ffxiv.housim.app.plugins.loader;

import com.jme3.audio.AudioData;
import com.jme3.util.NativeObject;
import ffxiv.housim.saintcoinach.sound.ScdEntry;
import ffxiv.housim.saintcoinach.sound.ScdEntryHeader;
import lombok.Getter;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/2
 */
public class ScdAudioData extends AudioData {

    @Getter
    private AudioData data;

    @Getter
    private Integer loopStartSample;
    @Getter
    private Integer loopEndSample;
    @Getter
    private Float loopStartSec;
    @Getter
    private Float loopEndSec;

    public ScdAudioData(AudioData delegate) {
        this.data = delegate;
    }

    public ScdAudioData(AudioData delegate, Integer loopStartSample, Integer loopEndSample) {
        this.data = delegate;
        int sampleRate = delegate.getSampleRate();
        if (loopStartSample != null) {
            this.loopStartSample = loopStartSample;
            this.loopStartSec = loopStartSample / (float) sampleRate;
        }
        if (loopEndSample != null) {
            this.loopEndSample = loopEndSample;
            this.loopEndSec = loopEndSample / (float) sampleRate;
        }
    }

    @Override
    public DataType getDataType() {
        return data.getDataType();
    }

    @Override
    public float getDuration() {
        return data.getDuration();
    }

    @Override
    public void resetObject() {
        data.resetObject();
    }

    @Override
    public void deleteObject(Object rendererObject) {
        data.deleteObject(rendererObject);
    }

    @Override
    public NativeObject createDestructableClone() {
        return data.createDestructableClone();
    }

    @Override
    public long getUniqueId() {
        return data.getUniqueId();
    }
}
