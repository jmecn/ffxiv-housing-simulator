package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.HalfHelper;
import ffxiv.housim.saintcoinach.math.Vector2;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector4;
import lombok.NonNull;

import java.nio.ByteBuffer;

public final class VertexReader {

    public static Vertex read(@NonNull ByteBuffer buffer, @NonNull VertexFormat format, @NonNull int[] offsets) {
        Vertex vertex = new Vertex();

        for (VertexFormatElement element : format.getElements()) {
            int elementOffset = offsets[element.sourcePart];

            readElement(buffer, element, elementOffset, vertex);
        }

        return vertex;
    }

    static void readElement(ByteBuffer buffer, VertexFormatElement element, int offset, Vertex vertex) {
        Object data = readData(buffer, element.dataType, offset + element.offset);

        switch (element.attribute) {
            case BlendIndices:
                vertex.blendIndices = (int) data;
                break;
            case BlendWeights:
                vertex.blendWeights = (Vector4)data;
                break;
            case Color:
                vertex.color = (Vector4)data;
                break;
            case Normal:
                vertex.normal = forceToVector3(data);
                break;
            case Position:
                vertex.position = forceToVector4(data);
                break;
            case Tangent2:
                vertex.tangent2 = (Vector4)data;
                break;
            case Tangent1:
                vertex.tangent1 = (Vector4)data;
                break;
            case UV:
                vertex.uv = forceToVector4(data);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    static Vector3 forceToVector3(Object value) {
        if (value instanceof Vector3)
        return (Vector3)value;
        if (value instanceof Vector4) {
            Vector4 v4 = (Vector4)value;
            return new Vector3 (
                v4.x, v4.y, v4.z
            );
        }
        throw new IllegalArgumentException();
    }

    static Vector4 forceToVector4(Object value) {
        if (value instanceof Vector4)
            return (Vector4)value;
        if (value instanceof Vector2) {
            Vector2 v2 = (Vector2)value;
            return new Vector4 (v2.x, v2.y, 0, 0);
        }
        if (value instanceof Vector3) {
            Vector3 v3 = (Vector3)value;
            return new Vector4 (v3.x, v3.y, v3.z, 1
            );
        }
        throw new IllegalArgumentException();
    }

    static Object readData(ByteBuffer buffer, VertexDataType type, int offset) {
        buffer.position(offset);
        switch (type) {
            case Half2:
                return new Vector2 (
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort())
                );
            case Half4:
                return new Vector4 (
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort())
                );
            case UInt:
                return buffer.getInt();
            case ByteFloat4:
                return new Vector4 (
                    buffer.get() / 255f,
                    buffer.get() / 255f,
                    buffer.get() / 255f,
                    buffer.get() / 255f
                );
            case Single3:
                return new Vector3(
                        buffer.getFloat(),
                        buffer.getFloat(),
                        buffer.getFloat()
                );
            case Single4:
                return new Vector4(
                        buffer.getFloat(),
                        buffer.getFloat(),
                        buffer.getFloat(),
                        buffer.getFloat()
                );
            default:
                throw new IllegalArgumentException();
        }
    }
}
