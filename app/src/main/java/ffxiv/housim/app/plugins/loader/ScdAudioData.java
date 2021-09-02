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
    private ScdEntryHeader header;

    public ScdAudioData(AudioData delegate, ScdEntry entry) {
        this.data = delegate;
        this.header = entry.getHeader();
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
