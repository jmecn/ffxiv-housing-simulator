package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.model.TransformedModel;
import ffxiv.housim.saintcoinach.scene.pcb.PcbFile;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbEntryModel implements ISgbEntry {
    // size 0x38 = 56 bytes
    public SgbEntryType type;
    public int gimmickId;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    public int modelFileOffset;
    public int collisionFileOffset;

    @Getter
    private final String name;
    @Getter
    private final String modelFilePath;
    @Getter
    private final String collisionFilePath;
    @Getter
    private TransformedModel model;
    @Getter
    private PcbFile collisionFile;

    public SgbEntryModel(PackCollection coll, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = SgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        modelFileOffset = buffer.getInt();
        collisionFileOffset = buffer.getInt();

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
        modelFilePath = ByteBufferStr.getString(buffer, offset + modelFileOffset);
        collisionFilePath = ByteBufferStr.getString(buffer, offset + collisionFileOffset);

        if (!modelFilePath.isEmpty()) {
            ModelFile mdlFile = (ModelFile) coll.tryGetFile(modelFilePath);
            model = new TransformedModel(mdlFile.getModelDefinition(), translation, rotation, scale);
        }

        if (!collisionFilePath.isEmpty()) {
            PackFile pcbFile = coll.tryGetFile(collisionFilePath);
            collisionFile = new PcbFile(pcbFile);
        }
    }

    @Override
    public SgbEntryType getType() {
        return type;
    }
}
