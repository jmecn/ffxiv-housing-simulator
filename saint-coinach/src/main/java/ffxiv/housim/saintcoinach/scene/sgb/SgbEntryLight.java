package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector2;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbEntryLight implements ISgbEntry {

    public SgbEntryType type;
    public int gimmickId;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    public int entryCount;
    public Vector2 entry1;
    public int unknownFlag1;
    public Vector2 entry2;
    public int entry2NameOffset;
    public short entry3NameOffset;
    public short unknownFlag2;
    public Vector2 entry3;
    public short unknownFlag3;
    public short unknownFlag4;
    public Vector2 entry4;
    public Vector2 entry5;
    // + unknowns

    @Getter
    private final String name;
    @Getter
    private final String entry2Name;
    @Getter
    private final String entry3Name;
    @Getter
    private SgbFile gimmick;

    public SgbEntryLight(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = SgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        entryCount = buffer.getInt();
        entry1 = new Vector2(buffer);
        unknownFlag1 = buffer.getInt();
        entry2 = new Vector2(buffer);
        entry2NameOffset = buffer.getInt();
        entry3NameOffset = buffer.getShort();
        unknownFlag2 = buffer.getShort();
        entry3 = new Vector2(buffer);
        unknownFlag3 = buffer.getShort();
        unknownFlag4 = buffer.getShort();
        entry4 = new Vector2(buffer);
        entry5 = new Vector2(buffer);

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
        entry2Name = ByteBufferStr.getString(buffer, offset + entry2NameOffset);
        entry3Name = ByteBufferStr.getString(buffer, offset + entry3NameOffset);

    }

    @Override
    public SgbEntryType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "#" + translation;
    }
}
