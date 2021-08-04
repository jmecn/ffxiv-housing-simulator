package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * Header of an index2 file.
 */
public class Index2Header {
    final static int FileDataOffset = 0x08;

    @Getter
    private int filesCount;
    @Getter
    private int filesOffset;

    public Index2Header(ByteBuffer reader) {
        int start = reader.position();

        reader.position(start + FileDataOffset);
        filesOffset = reader.getInt();
        int filesLength = reader.getInt();
        filesCount = filesLength / 0x08;
    }
}
