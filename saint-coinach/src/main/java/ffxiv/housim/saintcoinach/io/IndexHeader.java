package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.nio.ByteBuffer;

public class IndexHeader {
    final static int FileDataOffset = 0x08;
    final static int DirectoryDataOffset = 0xE4;

    @Getter
    private int directoriesCount;
    @Getter
    private int directoriesOffset;
    @Getter
    private int filesCount;
    @Getter
    private int filesOffset;

    public IndexHeader(ByteBuffer reader) {
        int start = reader.position();

        reader.position(start + FileDataOffset);
        filesOffset = reader.getInt();
        int filesLength = reader.getInt();
        filesCount = filesLength / 0x10;

        reader.position(start + DirectoryDataOffset);
        directoriesOffset = reader.getInt();
        int dirLength = reader.getInt();
        directoriesCount = dirLength / 0x10;
    }

}
