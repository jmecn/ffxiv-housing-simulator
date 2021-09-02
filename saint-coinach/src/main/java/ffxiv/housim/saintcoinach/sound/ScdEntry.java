package ffxiv.housim.saintcoinach.sound;

import lombok.Getter;

public abstract class ScdEntry {

    protected ScdFile file;

    @Getter
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
