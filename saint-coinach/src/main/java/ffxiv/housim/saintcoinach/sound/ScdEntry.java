package ffxiv.housim.saintcoinach.sound;

public abstract class ScdEntry {

    protected ScdFile file;
    protected ScdEntryHeader header;

    protected ScdEntry(ScdFile file, ScdEntryHeader header) {
        this.file = file;
        this.header = header;
    }

    public abstract byte[] getDecoded();

    @Override
    public String toString() {
        return String.format("%s, size:%d bytes", header.codec, getDecoded() != null ? getDecoded().length : 0);
    }
}
