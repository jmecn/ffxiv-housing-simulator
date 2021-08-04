package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Directory-entry inside an index file.
 */
public class IndexDirectory {
    @Getter
    private final PackIdentifier packId;
    @Getter
    private final int key;
    @Getter
    private final int offset;
    @Getter
    private final int count;
    @Getter
    private Map<Integer, IndexFile> files;

    public IndexDirectory(PackIdentifier packId, ByteBuffer reader) {
        this.packId = packId;

        key = reader.getInt();
        offset = reader.getInt();
        int len = reader.getInt();
        count = len / 0x10;

        reader.getInt();// skip zero

        int pos = reader.position();
        readFiles(reader);
        reader.position(pos);
    }

    private void readFiles(ByteBuffer reader) {
        reader.position(offset);

        int rem = count;
        List<IndexFile> files = new ArrayList<>(count);
        while(rem-- > 0) {
            files.add(new IndexFile(packId, reader));
        }

        this.files = new HashMap<>();
        for (IndexFile file : files) {
            this.files.put(file.getFileKey(), file);
        }
    }
}
