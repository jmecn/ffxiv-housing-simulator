package ffxiv.housim.saintcoinach.graphics.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector3;

import java.nio.ByteBuffer;

public class SgbGimmickEntry implements ISgbGroupEntry {
    public SgbGimmickEntry(PackCollection coll, ByteBuffer buffer, int entryOffset) {
    }

    public class HeaderData {
        public SgbGroupEntryType Type;
        public int GimmickId;
        public int NameOffset;
        public Vector3 Translation;
        public Vector3 Rotation;
        public Vector3 Scale;
        public int GimmickFileOffset;
        public int CollisionFileOffset;
        // SgbGimmickEntry size is around 152 bytes?
    }

    @Override
    public SgbGroupEntryType getType() {
        return null;
    }
}
