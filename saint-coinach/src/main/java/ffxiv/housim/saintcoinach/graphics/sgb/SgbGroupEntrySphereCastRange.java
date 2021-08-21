package ffxiv.housim.saintcoinach.graphics.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbGroupEntrySphereCastRange implements ISgbGroupEntry {

    // size 0x3C = 60 bytes
    @Getter
    private SgbGroupEntryType type;
    @Getter
    private int gimmickId;
    private int nameOffset;
    @Getter
    private Vector3 translation;
    @Getter
    private Vector3 rotation;
    @Getter
    private Vector3 scale;
    private int unknonw1;
    private short unknonw2;
    private short unknonw3;

    // following 4 bytes 0x00

    @Getter
    private final String name;

    public SgbGroupEntrySphereCastRange(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = SgbGroupEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        unknonw1 = buffer.getInt();
        unknonw2 = buffer.getShort();
        unknonw3 = buffer.getShort();

        // read name
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
    }

}
