package ffxiv.housim.saintcoinach.sound;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ScdAdpcmEntry extends ScdEntry {

    // all these are in big endian
    private static final int i_RIFF = 0x46464952;// RIFF
    private static final int i_WAVE = 0x45564157;// WAVE
    private static final int i_fmt  = 0x20746D66;// fmt
    private static final int i_data = 0x61746164;// data

    private byte[] decoded;

    protected ScdAdpcmEntry(ScdFile file, ScdEntryHeader header, int chunksOffset, int dataOffset) {
        super(file, header);
        decode(chunksOffset, dataOffset);
    }

    @Override
    public byte[] getDecoded() {
        return decoded;
    }

    private void decode(int chunksOffset, int dataOffset) {
        var wavHeaderOffset = dataOffset;
        var finalDataOffset = chunksOffset + header.samplesOffset;
        int fmtSize = finalDataOffset - wavHeaderOffset;

        decoded = new byte[0x1C + fmtSize + header.dataSize];
        ByteBuffer bb = ByteBuffer.wrap(decoded);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.putInt(i_RIFF);
        bb.putInt(0x14 + fmtSize + header.dataSize);
        bb.putInt(i_WAVE);
        bb.putInt(i_fmt);
        bb.putInt(fmtSize);
        var o = 0x14;
        System.arraycopy(file.data, wavHeaderOffset, decoded, o, fmtSize);

        o += fmtSize;
        bb.position(o);
        bb.putInt(i_data);
        bb.putInt(header.dataSize);

        o += 8;
        System.arraycopy(file.data, finalDataOffset, decoded, o, header.dataSize);
    }
}
