package ffxiv.housim.saintcoinach.graphics.shpk;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ShaderHeader {
    @Getter
    private int size;
    @Getter
    private ShaderType type;
    @Getter
    private int dataOffset;
    @Getter
    private int dataLength;
    @Getter
    private List<ShaderParameterReference> parameterReferences;

    public ShaderHeader(ShaderType type, ByteBuffer buffer) {
        this.size = 0x10;

        this.type = type;

        // 0x10 bytes
        this.dataOffset = buffer.getInt();
        this.dataLength = buffer.getInt();
        short cScalar = buffer.getShort();
        short cResource = buffer.getShort();
        int unknown1 = buffer.getInt();// skip 4 bytes padding

        List<ShaderParameterReference> paramRef = new ArrayList<>(cScalar + cResource);
        while (cScalar-- > 0) {
            paramRef.add(new ShaderParameterReference(ParameterType.Scalar, buffer));
            size += ShaderParameterReference.SIZE;
        }
        while (cResource-- > 0) {
            paramRef.add(new ShaderParameterReference(ParameterType.Resource, buffer));
            size += ShaderParameterReference.SIZE;
        }

        parameterReferences = List.copyOf(paramRef);
    }
}
