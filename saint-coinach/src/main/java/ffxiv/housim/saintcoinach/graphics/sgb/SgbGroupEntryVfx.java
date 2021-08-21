package ffxiv.housim.saintcoinach.graphics.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;

import java.nio.ByteBuffer;

public class SgbGroupEntryVfx implements ISgbGroupEntry {
    public SgbGroupEntryType type;
    public int unknownId;
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

    private String name;
    private String avfxFilePath;
    private PackFile avfxFile;

    public SgbGroupEntryVfx(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = SgbGroupEntryType.of(buffer.getInt());
        unknownId = buffer.getInt();
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
    public SgbGroupEntryType getType() {
        return type;
    }
}
