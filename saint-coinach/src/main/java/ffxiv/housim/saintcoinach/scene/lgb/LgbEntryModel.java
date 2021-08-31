package ffxiv.housim.saintcoinach.scene.lgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.model.TransformedModel;
import ffxiv.housim.saintcoinach.scene.pcb.PcbFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/31
 */
public class LgbEntryModel implements ILgbEntry {
    // size 0x50 = 80 bytes
    public LgbEntryType type;
    public int unknownId;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    public int modelFileOffset;
    public int collisionFileOffset;

    // paddings
    public int unknown0;
    public int unknown1;
    public int unknown2;
    public int unknown3;
    public int unknown4;
    public int unknown5;
    public int unknown6;
    public int unknown7;
    public float unknown8;

    @Getter
    private final String name;
    @Getter
    private final String modelFilePath;
    @Getter
    private final String collisionFilePath;

    private PackCollection packs;

    private boolean isMdlInitialized = false;
    private TransformedModel model;

    private boolean isPcbInitialized = false;
    private PcbFile collisionFile;

    LgbEntryModel(PackCollection coll, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        this.packs = coll;

        // read data
        type = LgbEntryType.of(buffer.getInt());
        unknownId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        modelFileOffset = buffer.getInt();
        collisionFileOffset = buffer.getInt();

        // paddings
        unknown0 = buffer.getInt();
        unknown1 = buffer.getInt();
        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
        unknown4 = buffer.getInt();
        unknown5 = buffer.getInt();
        unknown6 = buffer.getInt();
        unknown7 = buffer.getInt();
        unknown8 = buffer.getFloat();

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
        modelFilePath = ByteBufferStr.getString(buffer, offset + modelFileOffset);
        collisionFilePath = ByteBufferStr.getString(buffer, offset + collisionFileOffset);
    }

    public TransformedModel getModel() {

        if (!isMdlInitialized) {
            if (!modelFilePath.isEmpty()) {
                ModelFile mdlFile = (ModelFile) packs.tryGetFile(modelFilePath);
                if (mdlFile != null) {
                    model = new TransformedModel(mdlFile.getModelDefinition(), translation, rotation, scale);
                }
            }
            isMdlInitialized = true;
        }

        return model;
    }

    public PcbFile getCollisionFile() {
        if (!isPcbInitialized) {
            if (!collisionFilePath.isEmpty()) {
                PackFile pcbFile = packs.tryGetFile(collisionFilePath);
                if (pcbFile != null) {
                    collisionFile = new PcbFile(pcbFile);
                }
            }
            isPcbInitialized = true;
        }

        return collisionFile;
    }

    @Override
    public LgbEntryType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "#" + modelFilePath;
    }
}
