package ffxiv.housim.saintcoinach.material.imc;

import java.nio.ByteBuffer;

public class ImcVariant {

    public final static ImcVariant DEFAULT = new ImcVariant((short) 1, (short) 0x03FF, (short) 0);

    public short materialNum;// TODO: Only lower 8 bits are for v####, upper 8 bits unknown
    public short partVisibilityMask;
    public short effectNum;

    // internal use only
    ImcVariant(ByteBuffer buffer) {
        materialNum = buffer.getShort();
        partVisibilityMask = buffer.getShort();
        effectNum = buffer.getShort();
    }

    // for serialize
    public ImcVariant() {

    }

    public ImcVariant(short variant, short partVisibilityMask, short effectNum) {
        this.materialNum = variant;
        this.partVisibilityMask = partVisibilityMask;
        this.effectNum = effectNum;
    }
}
