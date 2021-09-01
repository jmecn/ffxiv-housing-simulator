package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.model.Model;
import ffxiv.housim.saintcoinach.scene.model.ModelQuality;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;

import java.nio.ByteBuffer;

public class SgbEntry1C implements ISgbEntry {

    public class HeaderData {// size 0x18 = 24 bytes
        public SgbEntryType type;
        public int nameOffset;
        public int index;
        public int unknown2;
        public int modelFileOffset;
        public int unknown3;

        HeaderData(ByteBuffer buffer) {
            type = SgbEntryType.of(buffer.getInt());
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

    private boolean isInitialized = false;
    private ModelFile mdlFile;
    private Model model;

    private SgbFile gimmick;

    private PackCollection packs;

    public SgbEntry1C(PackCollection packs, ByteBuffer buffer, int offset) {
        this.packs = packs;
        buffer.position(offset);
        header = new HeaderData(buffer);
        name = ByteBufferStr.getString(buffer, offset + header.nameOffset + 4 + 1);
        modelFilePath = ByteBufferStr.getString(buffer, offset + header.modelFileOffset + 4 + 1);

        load();
    }

    public Model getModel() {
        if (!isInitialized) {
            load();
        }
        return model;
    }

    public SgbFile getGimmick() {
        if (!isInitialized) {
            load();
        }
        return gimmick;
    }

    private void load() {
        isInitialized = true;
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
    public SgbEntryType getType() {
        return header.type;
    }
}
