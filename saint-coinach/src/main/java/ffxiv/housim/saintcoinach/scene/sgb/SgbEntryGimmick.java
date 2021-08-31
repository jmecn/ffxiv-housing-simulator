package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.model.TransformedModel;
import ffxiv.housim.saintcoinach.scene.pcb.PcbFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbEntryGimmick implements ISgbEntry {

    public SgbEntryType type;
    public int gimmickId;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    public int gimmickFileOffset;
    public int collisionFileOffset;
    // SgbEntryGimmick size is around 152 bytes?

    @Getter
    private final String name;
    @Getter
    private final String gimmickFilePath;
    @Getter
    private final String collisionFilePath;
    @Getter
    private SgbFile gimmick;
    @Getter
    private PcbFile collisionFile;

    public SgbEntryGimmick(PackCollection coll, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = SgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        gimmickFileOffset = buffer.getInt();
        collisionFileOffset = buffer.getInt();

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
        gimmickFilePath = ByteBufferStr.getString(buffer, offset + gimmickFileOffset);
        collisionFilePath = ByteBufferStr.getString(buffer, offset + collisionFileOffset);

        if (!gimmickFilePath.isEmpty()) {
            PackFile gimmickFile = coll.tryGetFile(gimmickFilePath);
            if (gimmickFile != null) {
                gimmick = new SgbFile(gimmickFile);
            }
        }

        if (!collisionFilePath.isEmpty()) {
            PackFile pcbFile = coll.tryGetFile(collisionFilePath);
            if (pcbFile != null) {
                collisionFile = new PcbFile(pcbFile);
            }
        }
    }

    @Override
    public SgbEntryType getType() {
        return type;
    }
}
