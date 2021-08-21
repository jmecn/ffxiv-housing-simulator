package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.graphics.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.graphics.mesh.MeshHeader;
import ffxiv.housim.saintcoinach.graphics.mesh.MeshPartHeader;
import ffxiv.housim.saintcoinach.graphics.pap.Bone;
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
    public final static int StringsCountOffset = 0x00;
    public final static int StringsSizeOffset = 0x04;
    public final static int StringsOffset = 0x08;

    public final static int ModelCount = 3;

    protected String[] materialNames;
    protected String[] attributeNames;

    private Model[] models = new Model[ModelCount];

    @Getter
    private List<ModelQuality> availableQualities;
    @Getter
    private ModelDefinitionHeader header;
    @Getter
    private ModelFile file;
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
    private ModelBoundingBoxes BoundingBoxes;
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

    private final static int FormatPart = 0;
    private final static int DefinitionPart = 1;

    private void build() {
        // osg and bil_*_base.mdl workaround
        // These models contain an extra 120 bytes after model headers
        boolean isOrg = file.getPath().contains("/osg_");

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

        int stringCount = buffer.getInt();
        int stringSize = buffer.getInt();

        int offset = StringsOffset + stringSize;// Skipping those, they'll be read further along the road

        buffer.position(offset);
        header = new ModelDefinitionHeader(buffer);

        unknownStructs1 = new ModelStruct1[header.unknownStruct1Count];
        for (int i = 0; i < header.unknownStruct1Count; i++) {
            unknownStructs1[i] = new ModelStruct1(buffer);
        }

        modelHeaders = new ModelHeader[ModelCount];
        for (int i = 0; i < ModelCount; i++) {
            modelHeaders[i] = new ModelHeader(buffer);
        }

        // Skip 120 bytes after model headers
        if (isOrg) {
            offset = buffer.position() + 120;
            buffer.position(offset);
        }

        availableQualities = new ArrayList<>(ModelCount);
        for (int i = 0; i < modelHeaders.length; i++) {
            if (modelHeaders[i].meshCount > 0) {
                availableQualities.add(ModelQuality.of(i));
            }
        }

        meshHeaders = new MeshHeader[header.meshCount];
        for (int i = 0; i < header.meshCount; i++) {
            meshHeaders[i] = new MeshHeader(buffer);
        }

        attributeNames = readStrings(buffer, header.attributeCount);
        attributes = new ModelAttribute[header.attributeCount];
        for (var i = 0; i < header.attributeCount; i++) {
            attributes[i] = new ModelAttribute(this, i);
        }

        unknownStructs2 = new ModelStruct2[header.unknownStruct2Count];
        for (int i = 0; i < header.unknownStruct2Count; i++) {
            unknownStructs2[i] = new ModelStruct2(buffer);
        }

        meshPartHeaders = new MeshPartHeader[header.partCount];
        for (int i = 0; i < header.partCount; i++) {
            meshPartHeaders[i] = new MeshPartHeader(buffer);
        }

        unknownStructs3 = new ModelStruct3[header.unknownStruct3Count];
        for (int i = 0; i < header.unknownStruct3Count; i++) {
            unknownStructs3[i] = new ModelStruct3(buffer);
        }

        materialNames = readStrings(buffer, header.materialCount);
        materials = new MaterialDefinition[header.materialCount];
        for (var i = 0; i < header.materialCount; i++) {
            materials[i] = new MaterialDefinition(this, i);
        }

        boneNames = readStrings(buffer, header.boneCount);
        boneLists = new BoneList[header.boneListCount];
        for (int i = 0; i < header.boneListCount; i++) {
            boneLists[i] = new BoneList(buffer);
        }

        unknownStructs5 = new ModelStruct5[header.unknownStruct5Count];
        for (int i = 0; i < header.unknownStruct5Count; i++) {
            unknownStructs5[i] = new ModelStruct5(buffer);
        }

        unknownStructs6 = new ModelStruct6[header.unknownStruct6Count];
        for (int i = 0; i < header.unknownStruct6Count; i++) {
            unknownStructs6[i] = new ModelStruct6(buffer);
        }

        unknownStructs7 = new ModelStruct7[header.unknownStruct7Count];
        for (int i = 0; i < header.unknownStruct7Count; i++) {
            unknownStructs7[i] = new ModelStruct7(buffer);
        }

        this.boneIndices = new BoneIndices(buffer);

        // Just padding, first byte specifying how many 0-bytes follow.
        int padding = buffer.get();
        buffer.position(buffer.position() + padding);

        this.BoundingBoxes = new ModelBoundingBoxes(buffer);

        this.bones = new Bone[header.boneCount];
        for (var i = 0; i < header.boneCount; i++)
            this.bones[i] = new Bone(this, i, buffer);

        if (buffer.position() != buffer.limit()) {
            //System.Diagnostics.Debugger.Break();    // Something's not right here.
        }

        buildVertexFormats();
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

        this.vertexFormats = new VertexFormat[header.meshCount];
        var offset = 0;
        for (var i = 0; i < header.meshCount; i++) {
            this.vertexFormats[i] = new VertexFormat(buffer);
        }
    }

    private static String[] readStrings(ByteBuffer buffer, int count) {
        String[] values = new String[count];
        for (var i = 0; i < count; i++) {
            int stringOffset = buffer.getInt();
            values[i] = ByteBufferStr.getString(buffer, StringsOffset + stringOffset);
        }
        return values;
    }
}
