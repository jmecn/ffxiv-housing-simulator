package ffxiv.housim.saintcoinach.scene.lgb;

import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.EObj;
import ffxiv.housim.saintcoinach.db.xiv.entity.ExportedSG;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.sgb.SgbFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/31
 */
@Slf4j
public class LgbEntryEObj implements ILgbEntry {

    public LgbEntryType type;
    public int unknown;
    public int nameOffset;
    public Vector3 translation;
    public Vector3 rotation;
    public Vector3 scale;
    @Getter
    public int eObjId;
    public int gimmickId;
    // 24 bytes of unknowns

    @Getter
    private final String name;

    private boolean isGimmickInitialized = false;
    private SgbFile gimmick;

    public LgbEntryEObj(PackCollection coll, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = LgbEntryType.of(buffer.getInt());
        unknown = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);
        eObjId = buffer.getInt();
        gimmickId = buffer.getInt();

        // parse data
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
    }

    public SgbFile getGimmick(XivCollection coll) {
        if (!isGimmickInitialized) {
            isGimmickInitialized = true;
            gimmick = null;

            if (eObjId == 0) {
                return null;
            }

            IXivSheet<EObj> sheet = coll.getSheet(EObj.class);
            EObj row = sheet.get(eObjId);
            if (row == null) {
                log.warn("EObj row not found, rowId={}", eObjId);
                return null;
            }

            ExportedSG sg= row.getSgbPath();
            if (sg == null) {
                return null;
            }

            String path = sg.getSgbPath();
            if (path == null || path.isEmpty()) {
                return null;
            }

            PackFile file = coll.getPackCollection().tryGetFile(path);
            if (file != null) {
                gimmick = new SgbFile(file);
            }
        }

        return gimmick;
    }

    @Override
    public LgbEntryType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "#" + eObjId;
    }
}
