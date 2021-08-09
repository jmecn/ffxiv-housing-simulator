package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class Index {

    @Getter
    private final PackIdentifier packId;

    @Getter
    private IndexHeader header;

    @Getter
    private Map<Integer, IndexDirectory> directories = new HashMap<>();

    public Index(PackIdentifier packId, String path) throws IOException {
        this.packId = packId;

        FileInputStream fileInputStream = new FileInputStream(path);

        ByteBuffer reader = ByteBuffer.allocate(fileInputStream.available());
        reader.order(ByteOrder.LITTLE_ENDIAN);

        FileChannel channel = fileInputStream.getChannel();
        channel.write(reader);

        build(reader);
    }

    public Index(PackIdentifier packId, FileInputStream fileInputStream) throws IOException {
        this.packId = packId;

        int length = fileInputStream.available();
        ByteBuffer reader = ByteBuffer.allocate(length);
        reader.order(ByteOrder.LITTLE_ENDIAN);

        FileChannel channel = fileInputStream.getChannel();
        channel.read(reader);
        reader.flip();

        build(reader);
    }

    public Index(PackIdentifier packId, ByteBuffer reader) {
        this.packId = packId;

        build(reader);
    }

    private void build(ByteBuffer reader) {
        long MAGIC = 0x00006B6361507153L;// SqPack
        long magic = reader.getLong();

        if (magic != MAGIC) {
            throw new IllegalArgumentException("Input stream is not a SqPack file.");
        }

        readHeader(reader);
        readDirectories(reader);
    }

    private void readHeader(ByteBuffer reader) {
        int HeaderOffsetOffset = 0x0C;
        reader.position(HeaderOffsetOffset);

        int headerOffset = reader.getInt();
        reader.position(headerOffset);

        header = new IndexHeader(reader);
    }

    private void readDirectories(ByteBuffer reader) {
        reader.position(header.getDirectoriesOffset());
        int rem = header.getDirectoriesCount();
        for (int i=0; i<rem; i++) {
            IndexDirectory dir = new IndexDirectory(packId, reader);
            directories.put(dir.getKey(), dir);
        }
    }
}
