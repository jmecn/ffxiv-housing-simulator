package ffxiv.housim.saintcoinach.graphics.sgb;

import ffxiv.housim.saintcoinach.graphics.ModelFile;
import ffxiv.housim.saintcoinach.graphics.model.TransformedModel;
import ffxiv.housim.saintcoinach.graphics.pcb.PcbFile;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbGroupEntryModel implements ISgbGroupEntry {
    public class HeaderData {// size 0x38 = 56 bytes
        public SgbGroupEntryType type;
        public int gimmickId;
        public int nameOffset;
        public Vector3 translation;
        public Vector3 rotation;
        public Vector3 scale;
        public int modelFileOffset;
        public int collisionFileOffset;

        HeaderData(ByteBuffer buffer) {
            type = SgbGroupEntryType.of(buffer.getInt());
            gimmickId = buffer.getInt();
            nameOffset = buffer.getInt();
            translation = new Vector3(buffer);
            rotation = new Vector3(buffer);
            scale = new Vector3(buffer);
            modelFileOffset = buffer.getInt();
            collisionFileOffset = buffer.getInt();
        }
    }

    private final HeaderData header;
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

    public SgbGroupEntryModel(PackCollection coll, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        header = new HeaderData(buffer);
        name = ByteBufferStr.getString(buffer, offset + header.nameOffset);
        modelFilePath = ByteBufferStr.getString(buffer, offset + header.modelFileOffset);
        collisionFilePath = ByteBufferStr.getString(buffer, offset + header.collisionFileOffset);

        if (!modelFilePath.isEmpty()) {
            ModelFile mdlFile = (ModelFile) coll.tryGetFile(modelFilePath);
            model = new TransformedModel(mdlFile.getModelDefinition(), header.translation, header.rotation, header.scale);
        }

        if (!collisionFilePath.isEmpty()) {
            PackFile pcbFile = coll.tryGetFile(collisionFilePath);
            collisionFile = new PcbFile(pcbFile);
        }
    }

    @Override
    public SgbGroupEntryType getType() {
        return header.type;
    }
}
