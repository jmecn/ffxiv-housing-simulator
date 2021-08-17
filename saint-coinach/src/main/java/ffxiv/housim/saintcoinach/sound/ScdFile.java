package ffxiv.housim.saintcoinach.sound;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class ScdFile {
    protected byte[] data;
    protected ByteBuffer buffer;
    @Getter
    private final PackFile sourceFile;
    @Getter
    private ScdHeader scdHeader;
    @Getter
    private ScdEntryHeader[] entryHeaders;
    @Getter
    private ScdEntry[] entries;

    public ScdFile(@NonNull PackFile sourceFile) {
        this.sourceFile = sourceFile;
        decode();
    }

    private void decode() {
        data = sourceFile.getData();
        buffer = ByteBuffer.wrap(data);

        init();

        var fileHeaderSize = buffer.getShort(0x0E);

        readScdHeader(fileHeaderSize);

        var entryHeaders = new ScdEntryHeader[scdHeader.entryCount];
        var entryTable = new int[scdHeader.entryCount];
        var entryChunkOffsets = new int[scdHeader.entryCount];
        var entryDataOffsets = new int[scdHeader.entryCount];

        buffer.position(scdHeader.entryTableOffset);
        for (var i = 0; i < scdHeader.entryCount; i++) {
            entryTable[i] = buffer.getInt();
        }

        for (int i = 0; i < scdHeader.entryCount; i++) {
            var headerOffset = entryTable[i];
            entryHeaders[i] = readEntryHeader(headerOffset);

            entryChunkOffsets[i] = headerOffset + 0x20;
            entryDataOffsets[i] = entryChunkOffsets[i];

            int dataOffset = buffer.getInt(entryDataOffsets[i] + 4);
            for (var j = 0; j < entryHeaders[i].auxChunkCount; j++) {
                entryDataOffsets[i] += dataOffset;
            }
        }
        this.entries = new ScdEntry[scdHeader.entryCount];
        for (var i = 0; i < scdHeader.entryCount; i++) {
            this.entries[i] = createEntry(entryHeaders[i], entryChunkOffsets[i], entryDataOffsets[i]);
        }

    }

    private void init() {
        buffer.order(ByteOrder.BIG_ENDIAN);
        long MAGIC = 0x5345444253534346L;// SEDBSSCF
        long magic = buffer.getLong();

        if (magic != MAGIC) {
            log.warn("Not scd file:{}", sourceFile.getPath());
            throw new IllegalArgumentException("Not scd file:" + sourceFile.getPath());
        }

        short verBigEndian = buffer.getShort(8);
        if (verBigEndian == 2 || verBigEndian == 3) {
            log.debug("BigEndian detected.");
            return;
        }

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        short verLittleEndian = buffer.getShort(8);
        if (verLittleEndian == 2 || verLittleEndian == 3) {
            log.debug("LittleEndian detected.");
            return;
        }

        log.warn("Unknown byte order: {}", sourceFile.getPath());
        throw new IllegalStateException("Unknown byte order:" + sourceFile.getPath());
    }
    private void readScdHeader(int offset) {
        var h = new ScdHeader();

        buffer.position(offset);

        // size 0x1C
        h.unknown1Count = buffer.getShort();
        h.unknown2Count = buffer.getShort();
        h.entryCount = buffer.getShort();
        h.unknown1 = buffer.getShort();
        h.unknown1Offset = buffer.getInt();
        h.entryTableOffset = buffer.getInt();
        h.unknown2Offset = buffer.getInt();
        h.unknown2 = buffer.getInt();
        h.unknownOffset1 = buffer.getInt();

        this.scdHeader = h;
    }

    private ScdEntryHeader readEntryHeader(int offset) {
        var h = new ScdEntryHeader();

        buffer.position(offset);

        // size 0x20
        h.dataSize = buffer.getInt();
        h.channelCount = buffer.getInt();
        h.frequency = buffer.getInt();
        h.codec = ScdCodec.of(buffer.getInt());
        h.loopStartSample = buffer.getInt();
        h.loopEndSample = buffer.getInt();
        h.samplesOffset = buffer.getInt();
        h.auxChunkCount = buffer.getShort();
        h.unknown1 = buffer.getShort();

        return h;
    }

    private ScdEntry createEntry(ScdEntryHeader header, int chunksOffset, int dataOffset) {
        if (header.dataSize == 0 || header.codec == ScdCodec.NONE)
            return null;

        switch (header.codec) {
            case OGG:
                return new ScdOggEntry(this, header, dataOffset);
            case MSADPCM:
                return new ScdAdpcmEntry(this, header, chunksOffset, dataOffset);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
