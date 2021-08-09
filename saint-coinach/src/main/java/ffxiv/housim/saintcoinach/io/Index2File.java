package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.nio.ByteBuffer;

public class Index2File implements IIndexFile {
    @Getter
    private PackIdentifier packId;
    @Getter
    private int fileKey;
    @Getter
    private int offset;
    @Getter
    private int datFile;// In which .dat* file the data is located.

    public Index2File(PackIdentifier packId, ByteBuffer reader) {
        this.packId = packId;

        fileKey = reader.getInt();
        int baseOffset = reader.getInt();

        datFile = (baseOffset & 0x7) >> 1;
        offset = (baseOffset & 0xFFFFFFF8) << 3;
    }

    @Override
    public int hashCode() {
        return ((datFile << 24) | packId.hashCode()) ^ offset;
    }

    @Override
    public String toString() {
        return String.format("Index2(fileKey=%08X, offset=%08X, dat=%d)", fileKey, offset, datFile);
    }
}
