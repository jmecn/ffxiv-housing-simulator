package ffxiv.housim.saintcoinach.scene.lgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class LgbEntryVfx implements ILgbEntry {
    public LgbEntryType type;
    public int gimmickId;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    public int avfxFileOffset;
    public int unknown5_2;
    public int unknown5_3;
    public int unknown5_4;
    public int unknown6;
    public Vector3 someVec3;
    // +unknowns

    @Getter
    private String name;
    @Getter
    private String avfxFilePath;
    @Getter
    private PackFile avfxFile;

    public LgbEntryVfx(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = LgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        avfxFileOffset = buffer.getInt();
        unknown5_2 = buffer.getInt();
        unknown5_3 = buffer.getInt();
        unknown5_4 = buffer.getInt();
        unknown6 = buffer.getInt();
        someVec3 = new Vector3(buffer);

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
        avfxFilePath = ByteBufferStr.getString(buffer, offset + avfxFileOffset);
        if (!avfxFilePath.isEmpty()) {
            avfxFile = packs.tryGetFile(avfxFilePath);
        }
    }

    @Override
    public LgbEntryType getType() {
        return type;
    }
}
