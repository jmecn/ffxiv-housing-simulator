package ffxiv.housim.saintcoinach.sound;

public abstract class ScdEntry {

    protected ScdFile file;
    protected ScdEntryHeader header;

    protected ScdEntry(ScdFile file, ScdEntryHeader header) {
        this.file = file;
        this.header = header;
    }

    public abstract byte[] getDecoded();
}
