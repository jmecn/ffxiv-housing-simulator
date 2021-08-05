package ffxiv.housim.saintcoinach.graphics;

import java.nio.ByteBuffer;

public final class VertexFormatElement {// 8 bytes
    public byte sourcePart;         // 0 = VertexData1 ; 1 = VertexData2 ; 255 = STAHP
    public byte offset;
    public VertexDataType dataType;
    public VertexAttribute attribute;
    public int unknown;             // Always 0 so far

    protected VertexFormatElement(ByteBuffer buffer) {
        sourcePart = buffer.get();
        offset = buffer.get();
        dataType = VertexDataType.of(buffer.get());
        attribute = VertexAttribute.of(buffer.get());
        unknown = buffer.getInt();
    }
}