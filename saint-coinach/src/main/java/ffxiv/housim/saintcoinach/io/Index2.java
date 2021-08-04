package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the data inside a *.index2 file.
 */
public class Index2 {

    @Getter
    private Index2Header header;
    @Getter
    private Map<Integer, Index2File> files = new HashMap<>();
    @Getter
    private PackIdentifier packId;

    public Index2(PackIdentifier packId, String path) throws IOException {
        this.packId = packId;

        FileInputStream fileInputStream = new FileInputStream(path);

        ByteBuffer reader = ByteBuffer.allocate(fileInputStream.available());
        reader.order(ByteOrder.LITTLE_ENDIAN);

        FileChannel channel = fileInputStream.getChannel();
        channel.write(reader);

        build(reader);
    }
    public Index2(PackIdentifier packId, FileInputStream fileInputStream) throws IOException {
        this.packId = packId;

        ByteBuffer reader = ByteBuffer.allocate(fileInputStream.available());
        reader.order(ByteOrder.LITTLE_ENDIAN);

        FileChannel channel = fileInputStream.getChannel();
        channel.write(reader);

        build(reader);
    }
    public Index2(PackIdentifier packId, ByteBuffer reader) throws IOException {
        this.packId = packId;

        build(reader);
    }


    private void build(ByteBuffer reader) {
        long MAGIC = 0x53715061636B0000L;// SqPack
        long magic = reader.getLong();

        if (magic != MAGIC) {
            throw new IllegalArgumentException("Input stream is not a SqPack file.");
        }

        readHeader(reader);
        readFiles(reader);
    }

    private void readHeader(ByteBuffer reader) {
        int HeaderOffsetOffset = 0x0C;
        reader.position(HeaderOffsetOffset);

        int headerOffset = reader.getInt();
        reader.position(headerOffset);

        header = new Index2Header(reader);
    }

    private void readFiles(ByteBuffer reader) {
        reader.position(header.getFilesOffset());
        int rem = header.getFilesCount();
        for (int i=0; i<rem; i++) {
            Index2File file = new Index2File(packId, reader);
            files.put(file.getFileKey(), file);
        }
    }
}
