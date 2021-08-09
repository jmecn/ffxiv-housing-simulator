package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * File entry inside an index file.
 */

public class IndexFile implements IIndexFile {
    @Getter
    private final PackIdentifier packId;
    @Getter
    private final int fileKey;
    @Getter
    private final int directoryKey;
    @Getter
    private final int offset;

    /**
     * In which .dat* file the data is located.
     */
    @Getter
    private final int datFile;

    public IndexFile(PackIdentifier packId, ByteBuffer reader) {

        this.packId = packId;

        fileKey = reader.getInt();
        directoryKey = reader.getInt();
        int baseOffset = reader.getInt();
        datFile = (baseOffset & 0xF) >> 1;
        offset = (baseOffset & 0xFFFFFFF8) << 3;

        reader.getInt(); // Zero
    }

    @Override
    public int hashCode() {
        return ((datFile << 24) | packId.hashCode()) ^ offset;
    }

    @Override
    public String toString() {
        return String.format("Index(fileKey=%08X, offset=%08X, dat=%d)", fileKey, offset, datFile);
    }
}
