package ffxiv.housim.saintcoinach.scene.model;

import ffxiv.housim.saintcoinach.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.scene.mesh.MeshHeader;
import ffxiv.housim.saintcoinach.scene.mesh.MeshPartHeader;
import ffxiv.housim.saintcoinach.scene.pap.Bone;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModelDefinition {
    public final static int StringsOffset = 0x08;

    public final static int ModelCount = 3;

    protected String[] materialNames;
    protected String[] attributeNames;

    private final Model[] models = new Model[ModelCount];

    @Getter
    private List<ModelQuality> availableQualities;

    int stringCount;
    int stringBlockSize;
    private String[] stringArray;

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

    @Getter
    private final ModelFile file;
    @Getter
    private VertexFormat[] vertexFormats;
    @Getter
    private ModelStruct1[] unknownStructs1;
    @Getter
    private ModelHeader[] modelHeaders;
    @Getter
    private MeshHeader[] meshHeaders;
    @Getter
    private ModelAttribute[] attributes;
    @Getter
    private ModelStruct2[] unknownStructs2;
    @Getter
    private MeshPartHeader[] meshPartHeaders;
    @Getter
    private ModelStruct3[] unknownStructs3;
    @Getter
    private MaterialDefinition[] materials;
    @Getter
    private String[] boneNames;
    @Getter
    private BoneList[] boneLists;
    @Getter
    private ModelStruct5[] unknownStructs5;
    @Getter
    private ModelStruct6[] unknownStructs6;
    @Getter
    private ModelStruct7[] unknownStructs7;
    @Getter
    private BoneIndices boneIndices;
    // Here's padding, but not keeping a variable amount of 0s
    @Getter
    private ModelBoundingBoxes boundingBoxes;
    @Getter
    private Bone[] bones;

    public ModelDefinition(ModelFile file) {
        this.file = file;
        build();
    }

    public Model getModel(int value) {
        ModelQuality quality = ModelQuality.of(value);
        return getModel(quality);
    }

    public Model getModel(ModelQuality quality) {
        int v = quality.value;
        if (models[v] == null) {
            try {
                models[v] = new Model(this, quality);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Failed load model, quality:{}", quality, e);
                return null;
            }
        }
        return models[v];
    }

    public String getMaterialName(int index) {
        return materialNames[index];
    }

    private final static int FormatPart = 0;
    private final static int DefinitionPart = 1;

    private void build() {
        // osg and bil_*_base.mdl workaround
        // These models contain an extra 120 bytes after model headers
        boolean isOrg = false;
        if (file.getPath() != null) {
            isOrg = file.getPath().contains("/osg_");
        }

        ByteBuffer buffer;
        try {
            byte[] data = file.getPart(DefinitionPart);
            buffer =  ByteBuffer.wrap(data);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Failed reading file", file.getPath());
            return;
        }

        readStrings(buffer);

        readHeader(buffer);

        unknownStructs1 = new ModelStruct1[unknownStruct1Count];
        for (int i = 0; i < unknownStruct1Count; i++) {
            unknownStructs1[i] = new ModelStruct1(buffer);
        }

        modelHeaders = new ModelHeader[ModelCount];
        for (int i = 0; i < ModelCount; i++) {
            modelHeaders[i] = new ModelHeader(buffer);
        }

        // Skip 120 bytes after model headers
        if (isOrg) {
            int offset = buffer.position() + 120;
            buffer.position(offset);
        }

        availableQualities = new ArrayList<>(ModelCount);
        for (int i = 0; i < modelHeaders.length; i++) {
            if (modelHeaders[i].meshCount > 0) {
                availableQualities.add(ModelQuality.of(i));
            }
        }

        meshHeaders = new MeshHeader[meshCount];
        for (int i = 0; i < meshCount; i++) {
            meshHeaders[i] = new MeshHeader(buffer);
        }

        attributeNames = readStrings(buffer, attributeCount);
        attributes = new ModelAttribute[attributeCount];
        for (int i = 0; i < attributeCount; i++) {
            attributes[i] = new ModelAttribute(this, i);
        }

        unknownStructs2 = new ModelStruct2[unknownStruct2Count];
        for (int i = 0; i < unknownStruct2Count; i++) {
            unknownStructs2[i] = new ModelStruct2(buffer);
        }

        meshPartHeaders = new MeshPartHeader[partCount];
        for (int i = 0; i < partCount; i++) {
            meshPartHeaders[i] = new MeshPartHeader(buffer);
        }

        unknownStructs3 = new ModelStruct3[unknownStruct3Count];
        for (int i = 0; i < unknownStruct3Count; i++) {
            unknownStructs3[i] = new ModelStruct3(buffer);
        }

        materialNames = readStrings(buffer, materialCount);
        // this is a hack
        for (int i = 0; i < materialCount; i++) {
            materialNames[i] = stringArray[i];
        }
        materials = new MaterialDefinition[materialCount];
        for (int i = 0; i < materialCount; i++) {
            materials[i] = new MaterialDefinition(this, i);
        }

        boneNames = readStrings(buffer, boneCount);
        boneLists = new BoneList[boneListCount];
        for (int i = 0; i < boneListCount; i++) {
            boneLists[i] = new BoneList(buffer);
        }

        unknownStructs5 = new ModelStruct5[unknownStruct5Count];
        for (int i = 0; i < unknownStruct5Count; i++) {
            unknownStructs5[i] = new ModelStruct5(buffer);
        }

        unknownStructs6 = new ModelStruct6[unknownStruct6Count];
        for (int i = 0; i < unknownStruct6Count; i++) {
            unknownStructs6[i] = new ModelStruct6(buffer);
        }

        unknownStructs7 = new ModelStruct7[unknownStruct7Count];
        for (int i = 0; i < unknownStruct7Count; i++) {
            unknownStructs7[i] = new ModelStruct7(buffer);
        }

        this.boneIndices = new BoneIndices(buffer);// TODO need more work
        if (boneIndices.dataSize > 0) {
            for (MeshHeader h : meshHeaders) {
                log.debug("vertexCount:{}", h.vertexCount);
            }
            log.debug("boneIndicesCount:{}", boneIndices.bones.length);
        }

        // Just padding, first byte specifying how many 0-bytes follow.
        int padding = buffer.get();
        buffer.position(buffer.position() + padding);

        try {
            this.boundingBoxes = new ModelBoundingBoxes(buffer);
        } catch (Exception e) {
            log.error("", e);
        }

        this.bones = new Bone[boneCount];
        for (int i = 0; i < boneCount; i++)
            this.bones[i] = new Bone(this, i, buffer);

        if (buffer.position() != buffer.limit()) {
            log.debug("Something's not right here. position:{}, limit:{}", buffer.position(), buffer.limit());
        }

        buildVertexFormats();
    }

    private void readHeader(ByteBuffer buffer) {
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

    private String[] readStrings(ByteBuffer buffer) {
        stringCount = buffer.getInt();

        // read data
        int stringBlockSize = buffer.getInt();
        byte[] stringBuffer = new byte[stringBlockSize];
        buffer.get(stringBuffer);

        stringArray = new String[stringCount];
        int stringCounter = 0;
        int start=0, end=0;
        for (int i = 0; i < stringBuffer.length; i++)
        {
            if (stringBuffer[i] == 0)
            {
                if (stringCounter >= stringCount)
                    break;
                stringArray[stringCounter] = new String(stringBuffer, start, end-start);
                start = end+1;
                stringCounter++;
            }
            end++;
        }
        log.debug("strings count:{}, values:{}", stringCount, stringArray);
        return stringArray;
    }
    private void buildVertexFormats() {
        ByteBuffer buffer;
        try {
            byte[] data = file.getPart(FormatPart);
            buffer =  ByteBuffer.wrap(data);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.vertexFormats = new VertexFormat[meshCount];
        for (int i = 0; i < meshCount; i++) {
            this.vertexFormats[i] = new VertexFormat(buffer);
        }
    }

    private static String[] readStrings(ByteBuffer buffer, int count) {

        int[] stringOffsets = new int[count];
        String[] values = new String[count];

        for (int i = 0; i < count; i++) {
            stringOffsets[i] = buffer.getInt();
        }

        int ptr = buffer.position();

        for (int i = 0; i < count; i++) {
            values[i] = ByteBufferStr.getString(buffer, StringsOffset + stringOffsets[i]);
        }
        log.debug("string offsets:{}, values:{}", stringOffsets, values);

        buffer.position(ptr);
        return values;
    }

    @Override
    public String toString() {
        return "ModelDefinition{" + file.getPath() + "}";
    }
}
