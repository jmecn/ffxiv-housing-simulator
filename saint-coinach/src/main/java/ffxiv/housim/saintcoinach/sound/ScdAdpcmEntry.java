package ffxiv.housim.saintcoinach.sound;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ScdAdpcmEntry extends ScdEntry {
    private byte[] decoded;

    protected ScdAdpcmEntry(ScdFile file, ScdEntryHeader header, int chunksOffset, int dataOffset) {
        super(file, header);
        decode(chunksOffset, dataOffset);
    }

    @Override
    public byte[] getDecoded() {
        return decoded;
    }

    final static int WaveHeaderSize = 0x10;

    private void decode(int chunksOffset, int dataOffset) {
        var wavHeaderOffset = dataOffset;
        var finalDataOffset = chunksOffset + header.samplesOffset;

        decoded = new byte[0x1C + WaveHeaderSize + header.dataSize];
        ByteBuffer bb = ByteBuffer.wrap(decoded);
        bb.order(ByteOrder.BIG_ENDIAN);
        var o = 0;
        decoded[o++] = (byte)'R';
        decoded[o++] = (byte)'I';
        decoded[o++] = (byte)'F';
        decoded[o++] = (byte)'F';

        bb.position(o);
        bb.putInt(0x14 + WaveHeaderSize + header.dataSize);

        o += 4;

        decoded[o++] = (byte)'W';
        decoded[o++] = (byte)'A';
        decoded[o++] = (byte)'V';
        decoded[o++] = (byte)'E';
        decoded[o++] = (byte)'f';
        decoded[o++] = (byte)'m';
        decoded[o++] = (byte)'t';
        decoded[o++] = (byte)' ';

        bb.position(o);
        bb.putInt(WaveHeaderSize);
        o += 4;

        System.arraycopy(file.data, wavHeaderOffset, decoded, o, WaveHeaderSize);
        o += WaveHeaderSize;

        decoded[o++] = (byte)'d';
        decoded[o++] = (byte)'a';
        decoded[o++] = (byte)'t';
        decoded[o++] = (byte)'a';

        bb.position(o);
        bb.putInt(header.dataSize);

        o += 4;
        System.arraycopy(file.data, finalDataOffset, decoded, o, header.dataSize);
    }
}
