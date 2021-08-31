package ffxiv.housim.saintcoinach.scene.lgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/31
 */
@Slf4j
public class LgbGroup {

    private int unknown1;
    private int nameOffset;
    private int entriesOffset;
    private int entryCount;

    private int unknown2;
    private int unknown3;
    private int festivalId;
    private int unknown5;
    // Just a guess -
    // This number corresponds to the last digits of Map.Id.  In
    // territories with rotated subdivisions, it can be used to select
    // the appropriate map for coordinate calculation.
    // Possibly 3-4 bytes to match the first three columns in the Map
    // exd.
    private int mapIndex;
    private int unknown7;
    private int unknown8;
    private int unknown9;

    private int unknown10;
    private int unknown11;

    private LgbFile parent;
    private String name;
    private ILgbEntry[] entries;

    public LgbGroup(LgbFile parent, ByteBuffer buffer, int offset) {
        this.parent = parent;

        PackCollection coll = parent.getFile().getPack().getCollection();

        buffer.position(offset);
        readHeader(buffer);

        name = ByteBufferStr.getString(buffer, offset + nameOffset);

        // read entry offset
        int[] entryOffsets = new int[entryCount];
        for (int i = 0; i < entryCount; i++) {
            entryOffsets[i] = offset + entriesOffset + buffer.getInt();
        }

        // read entries
        entries = new ILgbEntry[entryCount];
        for (int i = 0; i < entryCount; i++) {
            int entryOffset = entryOffsets[i];
            int entryType = buffer.getInt(entryOffset);

            LgbEntryType type = LgbEntryType.of(entryType);

            try {
                switch (type) {
                    case Model:
                        entries[i] = new LgbEntryModel(coll, buffer, entryOffset);
                        break;
                    case Gimmick:
                    case SharedGroup15:
                        entries[i] = new LgbEntryGimmick(coll, buffer, entryOffset);
                        break;
                    case EventObject:
                        entries[i] = new LgbEntryEObj(coll, buffer, entryOffset);
                        break;
                    case Light:
                        entries[i] = new LgbEntryLight(coll, buffer, entryOffset);
                        break;
                    case EventNpc:
                        entries[i] = new LgbEntryENpc(coll, buffer, entryOffset);
                        break;
                    case Vfx:
                        entries[i] = new LgbEntryVfx(coll, buffer, entryOffset);
                        break;
                    default:
                        log.debug("Unsupported lgb entry type:{}, value:{}, offset:{}, path:{}", type, String.format("%08X", entryType), entryOffset, parent.getFile().getPath());
                        break;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }


        log.info("load lgb group:{}, entries:{}", name, entries);
    }

    private void readHeader(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        nameOffset = buffer.getInt();
        entriesOffset = buffer.getInt();
        entryCount = buffer.getInt();

        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
        festivalId = buffer.getInt();
        unknown5 = buffer.getInt();

        mapIndex = buffer.getInt();
        unknown7 = buffer.getInt();
        unknown8 = buffer.getInt();
        unknown9 = buffer.getInt();

        unknown10 = buffer.getInt();
        unknown11 = buffer.getInt();
    }
}
