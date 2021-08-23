package ffxiv.housim.saintcoinach.material.shpk;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Shader {

    @Getter
    private ShaderType type;
    @Getter
    private int index;

    @Getter
    private int dataOffset;
    @Getter
    private int dataLength;
    private short numUniforms;
    private short numSamplers;
    private short numX;
    private short numY;

    @Getter
    private List<Parameter> parameters;

    public Shader(ShaderType type, int index, ByteBuffer buffer, int parameterListOffset) {

        this.type = type;
        this.index = index;

        // 0x10 bytes
        this.dataOffset = buffer.getInt();
        this.dataLength = buffer.getInt();
        this.numUniforms = buffer.getShort();
        this.numSamplers = buffer.getShort();
        this.numX = buffer.getShort();
        this.numY = buffer.getShort();

        // paramCount * 10 bytes
        int paramCount = numUniforms + numSamplers + numX + numY;
        parameters = new ArrayList<>(paramCount);

        for (int i = 0; i < numUniforms; i++) {
            parameters.add(new Parameter(ParameterType.Uniform, buffer, parameterListOffset));
        }

        for (int i = 0; i < numSamplers; i++) {
            parameters.add(new Parameter(ParameterType.Sampler, buffer, parameterListOffset));
        }

        for (int i = 0; i < numX; i++) {
            parameters.add(new Parameter(ParameterType.X, buffer, parameterListOffset));
        }

        for (int i = 0; i < numY; i++) {
            parameters.add(new Parameter(ParameterType.Y, buffer, parameterListOffset));
        }
    }

    @Override
    public String toString() {
        return type + "#" + index;
    }
}
