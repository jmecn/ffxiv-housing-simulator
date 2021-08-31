package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbEntryTargetMarker implements ISgbEntry {

    // size 0x38 = 56 bytes
    @Getter
    private SgbEntryType type;
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
    private int unknonw2;


    @Getter
    private final String name;

    public SgbEntryTargetMarker(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        type = SgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        unknonw1 = buffer.getInt();
        unknonw2 = buffer.getInt();

        name = ByteBufferStr.getString(buffer, offset + nameOffset);
    }

    @Override
    public SgbEntryType getType() {
        return type;
    }
}
