package ffxiv.housim.saintcoinach.sound;

public class ScdEntryHeader {
    public int dataSize;
    public int channelCount;
    public int frequency;
    public ScdCodec codec;
    public int loopStartSample;
    public int loopEndSample;
    public int samplesOffset;
    public short auxChunkCount;
    public short unknown1;
}
