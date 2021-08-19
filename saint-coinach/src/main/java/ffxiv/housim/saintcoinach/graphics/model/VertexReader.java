package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.utils.HalfHelper;
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
                vertex.blendWeights = (Vector4) data;
                break;
            case Color:
                vertex.color = (Vector4) data;
                break;
            case Normal:
                vertex.normal = forceToVector3(data);
                break;
            case Position:
                vertex.position = forceToVector4(data);
                break;
            case Tangent2:
                vertex.tangent2 = (Vector4) data;
                break;
            case Tangent1:
                vertex.tangent1 = (Vector4) data;
                break;
            case UV:
                vertex.uv = forceToVector4(data);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    static Vector3 forceToVector3(Object value) {
        if (value instanceof Vector3 v3) {
            return v3;
        }
        if (value instanceof Vector4 v4) {
            return new Vector3(v4);
        }
        throw new IllegalArgumentException();
    }

    static Vector4 forceToVector4(Object value) {
        if (value instanceof Vector4 v4) {
            return v4;
        }
        if (value instanceof Vector2 v2) {
            return new Vector4(v2, new Vector2(0));
        }
        if (value instanceof Vector3 v3) {
            return new Vector4(v3, 1);
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
                return new Vector3(buffer);
            case Single4:
                return new Vector4(buffer);
            default:
                throw new IllegalArgumentException();
        }
    }
}
