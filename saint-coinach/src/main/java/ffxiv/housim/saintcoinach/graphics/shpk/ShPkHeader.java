package ffxiv.housim.saintcoinach.graphics.shpk;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShPkHeader {
    public final static int SIZE = 0x48;

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

    @Getter
    private int scalarParameterCount;
    @Getter
    private int resourceParameterCount;

    @Getter
    private List<ShaderHeader> shaderHeaders;
    @Getter
    private List<ParameterHeader> parameterHeaders;

    public ShPkHeader(ByteBuffer buffer) {

        int magic = buffer.getInt();
        buffer.getInt();// skip
        int format = buffer.getInt();
        fileLength = buffer.getInt();

        if (magic != 0x6B506853) {
            log.warn("File is not a ShPk. magic:{}", String.format("%04X", magic));
        }
        if (format != 0x6B506853) {
            log.warn("Shader format is not supported. format:{}", String.format("%04X", format));
        }

        shaderDataOffset = buffer.getInt();
        parameterListOffset = buffer.getInt();
        vertexShaderCount = buffer.getInt();
        pixelShaderCount = buffer.getInt();

        int c0 = buffer.getInt();// skip 4 bytes
        int c1 = buffer.getInt();
        scalarParameterCount = buffer.getInt();
        resourceParameterCount = buffer.getInt();

        // skip 0x18 bytes
        buffer.position(SIZE);

        // read shader headers
        List<ShaderHeader> shs = new ArrayList<>(vertexShaderCount + pixelShaderCount);
        for (int i = 0; i < vertexShaderCount; i++) {
            shs.add(new ShaderHeader(ShaderType.Vertex, buffer));
        }
        for (int i = 0; i < pixelShaderCount; i++) {
            shs.add(new ShaderHeader(ShaderType.Pixel, buffer));
        }
        shaderHeaders = List.copyOf(shs);

        // skip c1 * 8 bytes
        for (int i = 0; i < c1; i++) {
            int v0 = buffer.getInt();
            int v1 = buffer.getInt();
        }

        List<ParameterHeader> phs = new ArrayList<>(scalarParameterCount + resourceParameterCount);
        for (int i = 0; i < scalarParameterCount; i++) {
            phs.add(new ParameterHeader(ParameterType.Scalar, buffer));
        }
        for (int i = 0; i < resourceParameterCount; i++) {
            phs.add(new ParameterHeader(ParameterType.Resource, buffer));
        }
        parameterHeaders = List.copyOf(phs);
    }
}
