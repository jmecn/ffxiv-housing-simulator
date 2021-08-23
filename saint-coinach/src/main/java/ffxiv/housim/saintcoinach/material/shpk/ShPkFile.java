package ffxiv.housim.saintcoinach.material.shpk;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShPkFile {

    // shader/shpk/bg.shpk
    // shader/shcd/rainbowvs.shcd
    // shader/posteffect/depth.shcd

    @Getter
    private int dxVer;
    @Getter
    private int fileLength;

    @Getter
    private int shaderDataOffset;
    @Getter
    private int parameterListOffset;
    @Getter
    private int vertexShaderCount;
    @Getter
    private int pixelShaderCount;

    private int count0;
    private int count1;
    @Getter
    private int numUniforms;
    @Getter
    private int numSamplers;

    private int count2;
    private int count3;
    private int count4;
    private int offset1;

    private int offset2;
    private int offset3;

    @Getter
    private List<Shader> vertexShaders;
    @Getter
    private List<Shader> pixelShaders;
    @Getter
    private List<Shader> shaders;
    @Getter
    private List<Parameter> parameters;

    @Getter
    private PackFile file;
    @Getter
    private ByteBuffer buffer;

    public ShPkFile(PackFile file) {
        this.file = file;

        byte[] bytes = file.getData();
        buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        readHeader(buffer);

        readShaders(buffer);

        // read in parameter names for the pack
        readParameters(buffer);

        // TODO: All the other things
    }

    /**
     * Read in ShPk header
     *
     * 0x48 bytes
     * @param buffer
     */
    private void readHeader(ByteBuffer buffer) {
        int magic = buffer.getInt();
        buffer.getInt();// skip
        dxVer = buffer.getInt();
        fileLength = buffer.getInt();

        if (magic != 0x6B506853) {// ShPk
            log.warn("File is not a ShPk. magic:{}", String.format("%04X", magic));
            throw new IllegalArgumentException("Not a ShPk file:" + file.getPath());
        }

        if (dxVer != 0x00395844) {// DX9
            log.warn("Unsupported version:{}", String.format("%08X", dxVer));
        }

        shaderDataOffset = buffer.getInt();
        parameterListOffset = buffer.getInt();
        vertexShaderCount = buffer.getInt();
        pixelShaderCount = buffer.getInt();

        count0 = buffer.getInt();// skip 4 bytes
        count1 = buffer.getInt();
        numUniforms = buffer.getInt();
        numSamplers = buffer.getInt();

        // skip 0x18 bytes padding
        count2 = buffer.getInt(); //Count?
        count3 = buffer.getInt(); //Count?
        count4 = buffer.getInt(); //Count?
        offset1 = buffer.getInt(); //Offsets?
        offset2 = buffer.getInt(); //Offsets?
        offset3 = buffer.getInt(); //Offsets?
    }

    /**
     * Read in parameters for shader pack
     *
     * @param buffer
     */
    private void readParameters(ByteBuffer buffer) {
        // skip c1 * 8 bytes
        buffer.position(buffer.position() + count1 * 8);
        //for (int i = 0; i < count1; i++) {
        //    int paramId = buffer.getInt();
        //    int offset = buffer.getInt();
        //}

        int paramCount = numUniforms + numSamplers;

        parameters = new ArrayList<>(paramCount);

        for (int i = 0; i < numUniforms; i++) {
            parameters.add(new Parameter(ParameterType.Uniform, buffer, parameterListOffset));
        }

        for (int i = 0; i < numSamplers; i++) {
            parameters.add(new Parameter(ParameterType.Sampler, buffer, parameterListOffset));
        }

    }

    /**
     * Read in shaders for the pack
     */
    private void readShaders(ByteBuffer buffer) {
        int shaderCount = vertexShaderCount + pixelShaderCount;
        vertexShaders = new ArrayList<>(vertexShaderCount);
        pixelShaders = new ArrayList<>(pixelShaderCount);
        shaders = new ArrayList<>(shaderCount);

        for (int i = 0; i < vertexShaderCount; i++) {
            Shader shader = new Shader(ShaderType.Vertex, i, buffer, parameterListOffset);
            vertexShaders.add(shader);
            shaders.add(shader);
        }

        for (int i = 0; i < pixelShaderCount; i++) {
            Shader shader = new Shader(ShaderType.Pixel, i, buffer, parameterListOffset);
            pixelShaders.add(shader);
            shaders.add(shader);
        }
    }

    public Shader getVertexShader(int index) {
        return vertexShaders.get(index);
    }

    public Shader getPixelShader(int index) {
        return pixelShaders.get(index);
    }

    public Shader getShader(int index) {
        return shaders.get(index);
    }

    /**
     * read shader bytecode
     */
    public byte[] getDXBC(Shader shader) {

        int vertexShaderOffset = (shader.getType() == ShaderType.Vertex ? 4 : 0);
        int offset = shaderDataOffset + shader.getDataOffset() + vertexShaderOffset;
        int len = shader.getDataLength()  - vertexShaderOffset;
        byte[] data = new byte[len];
        buffer.get(offset, data);

        return data;
    }
}
