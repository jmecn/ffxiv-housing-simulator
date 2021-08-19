package ffxiv.housim.saintcoinach.math;

import java.nio.ByteBuffer;

public class IndexData {
    public byte index1;
    public byte index2;
    public byte index3;
    public byte unknown1;
    public byte unknown2;
    public byte unknown3;
    public byte unknown4;
    public byte unknown5;
    public byte unknown6;
    public byte unknown7;
    public byte unknown8;
    public byte unknown9;

    public IndexData(ByteBuffer buffer) {
        index1 = buffer.get();
        index2 = buffer.get();
        index3 = buffer.get();
        unknown1 = buffer.get();
        unknown2 = buffer.get();
        unknown3 = buffer.get();
        unknown4 = buffer.get();
        unknown5 = buffer.get();
        unknown6 = buffer.get();
        unknown7 = buffer.get();
        unknown8 = buffer.get();
        unknown9 = buffer.get();
    }
}
