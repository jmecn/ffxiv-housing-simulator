package ffxiv.housim.saintcoinach.graphics.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

public class SgbGroupEntryChairMarker implements ISgbGroupEntry {

    public class HeaderData {// size 0x38 = 56 bytes
        public SgbGroupEntryType type;
        public int gimmickId;
        public int nameOffset;
        public Vector3 translation;
        public Vector3 rotation;
        public Vector3 scale;
        public int unknonw1;
        public int unknonw2;

        HeaderData(ByteBuffer buffer) {
            type = SgbGroupEntryType.of(buffer.getInt());
            gimmickId = buffer.getInt();
            nameOffset = buffer.getInt();
            translation = new Vector3(buffer);
            rotation = new Vector3(buffer);
            scale = new Vector3(buffer);
            unknonw1 = buffer.getInt();
            unknonw2 = buffer.getInt();
        }
    }

    private final HeaderData header;
    @Getter
    private final String name;

    public SgbGroupEntryChairMarker(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);
        header = new HeaderData(buffer);
        name = ByteBufferStr.getString(buffer, offset + header.nameOffset);
    }

    @Override
    public SgbGroupEntryType getType() {
        return header.type;
    }
}
