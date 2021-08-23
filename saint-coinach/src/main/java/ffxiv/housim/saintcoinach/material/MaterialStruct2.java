package ffxiv.housim.saintcoinach.material;

import java.nio.ByteBuffer;

public class MaterialStruct2 {
    public int unknown1;   // Probably an Id of some sort
    public short offset;   // Offset & Size are relative to the blob of data at the end, after ParameterMappings
    public short size;

    public MaterialStruct2(ByteBuffer buffer) {
        unknown1 = buffer.getInt();
        offset = buffer.getShort();
        size = buffer.getShort();
    }
}
