package ffxiv.housim.saintcoinach.scene.lgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.sgb.SgbFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/31
 */
public class LgbEntryENpc implements ILgbEntry {

    public LgbEntryType type;
    public int gimmickId;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    public int eNpcId;
    // 24 bytes of unknowns

    @Getter
    private final String name;

    public LgbEntryENpc(PackCollection coll, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = LgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        eNpcId = buffer.getInt();

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
    }

    @Override
    public LgbEntryType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "#" + eNpcId;
    }
}
