package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbGroupEntryTargetMarker implements ISgbGroupEntry {

    // size 0x38 = 56 bytes
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
    private int unknonw2;


    @Getter
    private final String name;

    public SgbGroupEntryTargetMarker(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        type = SgbGroupEntryType.of(buffer.getInt());
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
    public SgbGroupEntryType getType() {
        return type;
    }
}
