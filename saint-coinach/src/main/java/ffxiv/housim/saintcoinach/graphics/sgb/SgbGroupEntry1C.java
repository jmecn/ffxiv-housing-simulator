package ffxiv.housim.saintcoinach.graphics.sgb;

import ffxiv.housim.saintcoinach.graphics.model.ModelFile;
import ffxiv.housim.saintcoinach.graphics.model.Model;
import ffxiv.housim.saintcoinach.graphics.model.ModelQuality;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;

import java.nio.ByteBuffer;

public class SgbGroupEntry1C implements ISgbGroupEntry {

    public class HeaderData {// size 0x18 = 24 bytes
        public SgbGroupEntryType type;
        public int nameOffset;
        public int index;
        public int unknown2;
        public int modelFileOffset;
        public int unknown3;

        HeaderData(ByteBuffer buffer) {
            type = SgbGroupEntryType.of(buffer.getInt());
            nameOffset = buffer.getInt();
            index = buffer.getInt();
            unknown2 = buffer.getInt();
            modelFileOffset = buffer.getInt();
            unknown3 = buffer.getInt();
        }
    }

    private HeaderData header;
    private String name;
    private String modelFilePath;
    private Model model;
    private SgbFile gimmick;

    public SgbGroupEntry1C(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);
        header = new HeaderData(buffer);
        name = ByteBufferStr.getString(buffer, offset + header.nameOffset + 4 + 1);
        modelFilePath = ByteBufferStr.getString(buffer, offset + header.modelFileOffset + 4 + 1);
        if (!modelFilePath.isEmpty()) {
            if (modelFilePath.endsWith(".mdl")) {
                PackFile file = packs.tryGetFile(modelFilePath);
                if (file != null && file instanceof ModelFile mdlFile) {
                    model = mdlFile.getModelDefinition().getModel(ModelQuality.High);
                }
            } else if (modelFilePath.endsWith(".sgb")) {
                PackFile file = packs.tryGetFile(modelFilePath);
                if (file != null) {
                    gimmick = new SgbFile(file);
                }
            }
        }
    }

    @Override
    public SgbGroupEntryType getType() {
        return header.type;
    }
}
