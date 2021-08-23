package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.List;

@Slf4j
@Getter
public class SgbGroup implements ISgbData {

    public class HeaderData {
        public SgbDataType type;
        public int nameOffset;
        public int unknown08;
        public int unknown0C;

        public int unknown10;
        public int unknown14;
        public int unknown18;
        public int unknown1C;

        public int entryCount;
        public int unknown24;
        public int unknown28;
        public int unknown2C;

        public int unknown30;
        public int unknown34;
        public int unknown38;
        public int unknown3C;

        public int unknown40;
        public int unknown44;

        HeaderData(ByteBuffer buffer) {
            type = SgbDataType.of(buffer.getInt());
            nameOffset = buffer.getInt();
            unknown08 = buffer.getInt();
            unknown0C = buffer.getInt();

            unknown10 = buffer.getInt();
            unknown14 = buffer.getInt();
            unknown18 = buffer.getInt();
            unknown1C = buffer.getInt();

            entryCount = buffer.getInt();
            unknown24 = buffer.getInt();
            unknown28 = buffer.getInt();
            unknown2C = buffer.getInt();

            unknown30 = buffer.getInt();
            unknown34 = buffer.getInt();
            unknown38 = buffer.getInt();
            unknown3C = buffer.getInt();

            unknown40 = buffer.getInt();
            unknown44 = buffer.getInt();
        }
    }

    public class Offset1CHeaderData {
        public SgbDataType type;
        public int nameOffset;
        public int unknown08;
        public int entryCount;

        public int unknown14;
        public int modelFileOffset;
        public Vector3 unknownFloat3;
        public Vector3 unknownFloat3_2;
        public int stateOffset;
        public int modelFileOffset2;
        public int unknown3;
        public float unknown4;
        public int nameOffset2;
        public Vector3 unknownFloat3_3;
        public float unknown5;

        Offset1CHeaderData(ByteBuffer buffer) {
            type = SgbDataType.of(buffer.getInt());
            nameOffset = buffer.getInt();
            unknown08 = buffer.getInt();

            entryCount = buffer.getInt();
            unknown14 = buffer.getInt();
            modelFileOffset = buffer.getInt();
            unknownFloat3 = new Vector3(buffer);
            unknownFloat3_2 = new Vector3(buffer);
            stateOffset = buffer.getInt();
            modelFileOffset2 = buffer.getInt();
            unknown3 = buffer.getInt();
            unknown4 = buffer.getFloat();
            nameOffset2 = buffer.getInt();
            unknownFloat3_3 = new Vector3(buffer);
            unknown5 = buffer.getFloat();
        }
    }

    private HeaderData header;
    private Offset1CHeaderData offset1CHeader;
    private String name;
    private String modelFile1;
    private String modelFile2;
    private String modelFile3;
    private List<String> states;
    private SgbFile parent;
    private ISgbGroupEntry[] entries;

    public SgbGroup(SgbFile parent, ByteBuffer buffer, int offset) {
        this.parent = parent;
        int entriesOffset = offset;
        int count = 0;

        PackCollection coll = parent.getFile().getPack().getCollection();

        buffer.position(entriesOffset);
        this.header = new HeaderData(buffer);
        entriesOffset = buffer.position();

        count = header.entryCount;
        ISgbGroupEntry[] entries = new ISgbGroupEntry[count];
        int[] entryOffsets = new int[count];
        for (int i = 0; i < count; i++) {
            entryOffsets[i] = buffer.getInt();
        }
        log.debug("headerSize:{}, entryCount:{}, entryTableSize:{}, ptr:{}", entriesOffset, count, count * 4, buffer.position());

        for (int i = 0; i < count; i++) {
            int size = -1;
            try {
                if (i < count - 1) {
                    size = entryOffsets[i + 1] - entryOffsets[i];
                }
                int entryOffset = entriesOffset + entryOffsets[i];
                int entryType = buffer.getInt(entryOffset);
                SgbGroupEntryType type = SgbGroupEntryType.of(entryType);

                int entrySize = size > 0 ? size : type.size;
                log.debug("entry#{}, offset:{}, entryOffset:{}, type:{}, entrySize={}", i, entryOffsets[i], entryOffset, type, entrySize);
                switch (type) {
                    case Model:
                        entries[i] = new SgbGroupEntryModel(coll, buffer, entryOffset);
                        break;
                    case Gimmick:
                        entries[i] = new SgbGimmickEntry(coll, buffer, entryOffset);
                        break;
                    case Sound:
                        entries[i] = new SgbGroupEntrySound(coll, buffer, entryOffset);
                        break;
                    case Light:
                        entries[i] = new SgbGroupEntryLight(coll, buffer, entryOffset);
                        break;
                    case Vfx:
                        entries[i] = new SgbGroupEntryVfx(coll, buffer, entryOffset);
                        break;
                    case TargetMarker:
                        entries[i] = new SgbGroupEntryTargetMarker(coll, buffer, entryOffset);
                        break;
                    case ChairMarker:
                        entries[i] = new SgbGroupEntryChairMarker(coll, buffer, entryOffset);
                        break;
                    case ClickableRange:
                        entries[i] = new SgbGroupEntryClickableRange(coll, buffer, entryOffset);
                        break;
                    case SphereCastRange:
                        entries[i] = new SgbGroupEntrySphereCastRange(coll, buffer, entryOffset);
                        break;
                    default:
                        log.warn("Unsupported sgb entry type:{}, value:{}, offset:{}, size:{}, path:{}", type, entryType, entryOffset, entrySize, parent.getFile().getPath());
                        break;
                    // TODO: Work out other parts.
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

        this.entries = entries;

        this.name = ByteBufferStr.getString(buffer, offset + header.nameOffset);
    }

    public SgbGroup(SgbFile parent, ByteBuffer buffer, int offset, boolean isOffset1C) {
        this.parent = parent;
        int entriesOffset = offset;
        int count = 0;

        PackCollection coll = parent.getFile().getPack().getCollection();

        buffer.position(entriesOffset);
        this.header = new HeaderData(buffer);
        buffer.position(entriesOffset);
        this.offset1CHeader = new Offset1CHeaderData(buffer);
        entriesOffset = buffer.position();

        count = offset1CHeader.entryCount;
        entries = new ISgbGroupEntry[count];

        log.debug("headerSize:{}, entryCount:{}, entrySize:{}, ptr:{}", entriesOffset - offset, count, count * 24, entriesOffset);

        for (var i = 0; i < count; i++) {
            try {
                var entryOffset = entriesOffset + (i * 24);
                entries[i] = new SgbGroupEntry1C(coll, buffer, entryOffset);
                break;
            } catch (Exception e) {
                log.error("", e);
            }
        }

        this.name = ByteBufferStr.getString(buffer, offset + offset1CHeader.nameOffset);
        this.modelFile1 = ByteBufferStr.getString(buffer, offset + offset1CHeader.modelFileOffset + 1);
        this.modelFile2 = ByteBufferStr.getString(buffer, offset + offset1CHeader.modelFileOffset2 + 1);
        this.modelFile3 = ByteBufferStr.getString(buffer, offset + offset1CHeader.nameOffset2 + 64);
    }

    @Override
    public SgbDataType getType() {
        return header.type;
    }
}
