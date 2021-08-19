package ffxiv.housim.saintcoinach.graphics.model;

import java.nio.ByteBuffer;

public class ModelDefinitionHeader {
    public int unknown1;
    public short meshCount;
    public short attributeCount;
    public short partCount;
    public short materialCount;
    public short boneCount;
    public short boneListCount;  // 3 in hsl
    public short unknownStruct5Count;  // 4 in hsl
    public short unknownStruct6Count;  // 5 in hsl
    public short unknownStruct7Count;  // 6 in hsl
    public short unknown2;
    public short unknownStruct1Count;  // 0 in hsl
    public byte unknownStruct2Count;  // 1 in hsl
    public byte unknown3;
    public short[] unknown4 = new short[5];
    public short unknownStruct3Count;  // 7 in hsl
    public short[] unknown5 = new short[8];

    public ModelDefinitionHeader(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        meshCount = buffer.getShort();
        attributeCount = buffer.getShort();
        partCount = buffer.getShort();
        materialCount = buffer.getShort();
        boneCount = buffer.getShort();
        boneListCount = buffer.getShort();
        unknownStruct5Count = buffer.getShort();
        unknownStruct6Count = buffer.getShort();
        unknownStruct7Count = buffer.getShort();
        unknown2 = buffer.getShort();
        unknownStruct1Count = buffer.getShort();
        unknownStruct2Count = buffer.get();
        unknown3 = buffer.get();
        for (int i = 0; i < 5; i++) {
            unknown4[i] = buffer.getShort();
        }
        unknownStruct3Count = buffer.getShort();
        for (int i = 0; i < 8; i++) {
            unknown5[i] = buffer.getShort();
        }
    }
}
