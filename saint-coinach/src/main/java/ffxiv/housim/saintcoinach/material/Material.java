package ffxiv.housim.saintcoinach.material;

import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.material.shpk.ShPkFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class Material {
    private final static String DummyTextureInMaterial = "dummy.tex";

    private final static String DummyTexturePath = "common/graphics/texture/dummy.tex";

    ///////// Material data

    // header
    private int unknownHeader;// 4 bytes 0x00 0x00 0x03 0x01
    private short fileSize;
    @Getter
    private short colorSetDataSize;// 0 or 512 bytes
    private short stringsSize;
    private short shaderOffset;
    private byte texCount;
    private byte mapCount;
    private byte colorSetCount;
    private byte unknownSize;

    private String[] texNames;
    private String[] maps;
    private String[] colorSets;
    private String shader;// bg.shpk
    private byte[] unknown;
    private byte[] colorSetData;// half float rgba color, width 4 * height 16 in pixel size

    // metadata
    private short dataSize;// Bytes at the end
    private short struct1Count;
    private short struct2Count;
    private short parameterMappingCount;
    private short unknown1;
    private short unknown2;

    private MaterialStruct1[] structs1;
    private MaterialStruct2[] structs2;
    @Getter
    private MaterialTextureParameter[] textureParameters;
    private byte[] data;

    //////////// Material data end

    @Getter
    private MaterialDefinition definition;
    @Getter
    private PackFile file;
    @Getter
    private ImcVariant variant;
    @Getter
    private ImageFile[] textureFiles;// related texture files

    public Material(MaterialDefinition definition, PackFile file, ImcVariant variant) {
        this.definition = definition;
        this.file = file;
        this.variant = variant;

        build();
    }

    private void build() {
        log.info("file:{}", file.getPath());

        byte[] bytes = file.getData();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // read header
        readHeader(buffer);

        // read names of texture, map, color set and shader
        readNames(buffer);

        // read ColorSet data
        this.unknown = new byte[unknownSize];
        buffer.get(unknown);

        this.colorSetData = new byte[colorSetDataSize];
        buffer.get(colorSetData);

        // read metadata
        readMetadata(buffer);

        // load ImageFiles
        loadTextures();
    }

    private void readHeader(ByteBuffer buffer) {
        unknownHeader = buffer.getInt();
        fileSize = buffer.getShort();
        colorSetDataSize = buffer.getShort();
        stringsSize = buffer.getShort();
        shaderOffset = buffer.getShort();
        texCount = buffer.get();
        mapCount = buffer.get();
        colorSetCount = buffer.get();
        unknownSize = buffer.get();
    }

    private void readNames(ByteBuffer buffer) {
        int[] texOffset = new int[texCount];
        for (var i = 0; i < texCount; i++) {
            texOffset[i] = buffer.getShort();
            buffer.getShort();// 0 or 1, what to do with this?
        }

        int[] mapOffset = new int[mapCount];
        for (var i = 0; i < mapCount; i++) {
            mapOffset[i] = buffer.getShort();
            buffer.getShort();// 0 or 1, what to do with this?
        }

        int[] colorSetOffset = new int[colorSetCount];
        for (var i = 0; i < colorSetCount; i++) {
            colorSetOffset[i] = buffer.getShort();
            buffer.getShort();// 0 or 1, what to do with this?
        }

        // read string data
        byte[] stringsData = new byte[stringsSize];
        buffer.get(stringsData);
        ByteBuffer stringBuffer = ByteBuffer.wrap(stringsData);

        this.texNames = new String[texCount];
        for (int i = 0; i < texCount; i++) {
            texNames[i] = ByteBufferStr.getString(stringBuffer, texOffset[i]);
        }

        this.maps = new String[mapCount];
        for (int i = 0; i < mapCount; i++) {
            maps[i] = ByteBufferStr.getString(stringBuffer, mapOffset[i]);
        }

        this.colorSets = new String[colorSetCount];
        for (int i = 0; i < colorSetCount; i++) {
            colorSets[i] = ByteBufferStr.getString(stringBuffer, colorSetOffset[i]);
        }

        this.shader = ByteBufferStr.getString(stringBuffer, shaderOffset);
    }

    private void readMetadata(ByteBuffer buffer) {
        // header
        dataSize = buffer.getShort();
        struct1Count = buffer.getShort();
        struct2Count = buffer.getShort();
        parameterMappingCount = buffer.getShort();
        unknown1 = buffer.getShort();
        unknown2 = buffer.getShort();

        // struct1
        structs1 = new MaterialStruct1[struct1Count];
        for (int i = 0; i < struct1Count; i++) {
            structs1[i] = new MaterialStruct1(buffer);
        }

        // struct2
        structs2 = new MaterialStruct2[struct2Count];
        for (int i = 0; i < struct2Count; i++) {
            structs2[i] = new MaterialStruct2(buffer);
        }

        // texture parameters
        textureParameters = new MaterialTextureParameter[parameterMappingCount];
        for (int i = 0; i < parameterMappingCount; i++) {
            textureParameters[i] = new MaterialTextureParameter(buffer);
        }

        // data
        this.data = new byte[dataSize];
        buffer.get(data);
    }

    private void loadTextures() {
        PackCollection packs = file.getPack().getCollection();

        textureFiles = new ImageFile[texCount];
        for (int i = 0; i < texCount; i++) {
            String fileName = texNames[i];
            if (DummyTextureInMaterial.equals(fileName)) {
                fileName = DummyTexturePath;
            }

            textureFiles[i] = (ImageFile) packs.tryGetFile(fileName);
        }
    }

    public ShPkFile getShPk() {
        PackCollection packs = file.getPack().getCollection();
        String shaderPack = "shader/shpk/" + shader;

        PackFile file = packs.tryGetFile(shaderPack);
        if (file != null) {
            log.info("load shpk:{}", shaderPack);
            return new ShPkFile(file);
        }

        return null;
    }
}
