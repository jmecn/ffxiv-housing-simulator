package ffxiv.housim.saintcoinach.graphics;

import java.nio.ByteBuffer;

public class ImcVariant {

    public final static ImcVariant DEFAULT = new ImcVariant((short) 1, (short) 0x03FF, (byte) 0, (byte) 0);

    public short variant;// TODO: Only lower 8 bits are for v####, upper 8 bits unknown
    public short partVisibilityMask;
    public byte unknown3;
    public byte unknown4;

    // internal use only
    ImcVariant(ByteBuffer buffer) {
        variant = buffer.getShort();
        partVisibilityMask = buffer.getShort();
        unknown3 = buffer.get();
        unknown4 = buffer.get();
    }

    // for serialize
    public ImcVariant() {

    }

    public ImcVariant(short variant, short partVisibilityMask, byte unknown3, byte unknown4) {
        this.variant = variant;
        this.partVisibilityMask = partVisibilityMask;
        this.unknown3 = unknown3;
        this.unknown4 = unknown4;
    }
}
