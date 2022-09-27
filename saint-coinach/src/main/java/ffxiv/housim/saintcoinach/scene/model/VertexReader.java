package ffxiv.housim.saintcoinach.scene.model;

import ffxiv.housim.saintcoinach.math.Ubyte4;
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
            case Position : {
                vertex.position = (Vector4) data;
                break;
            }
            case BoneWeights : {
                vertex.boneWeights = (Vector4) data;
                break;
            }
            case BoneIndices : {
                vertex.boneIndices = (Ubyte4) data;
                break;
            }
            case Normal : {
                vertex.normal = (Vector4) data;
                break;
            }
            case TexCoord : {
                vertex.texCoord = forceToVector4(data);
                break;
            }
            case Tangent : {
                vertex.tangent = (Vector4) data;
                break;
            }
            case Binormal : {
                vertex.binormal = (Vector4) data;
                break;
            }
            case Color : {
                vertex.color = (Vector4) data;
                break;
            }
            default : {
                throw new IllegalArgumentException();
            }
        }
    }

    static Vector4 forceToVector4(Object value) {
        if (value instanceof Vector4) {
            return (Vector4) value;
        }
        if (value instanceof Vector2) {
            Vector2 v2 = (Vector2) value;
            return new Vector4(v2, new Vector2(0));
        }
        throw new IllegalArgumentException();
    }

    static Object readData(ByteBuffer buffer, VertexDataType type, int offset) {
        buffer.position(offset);
        switch (type) {
            case Float3 : {
                return new Vector3(buffer);
            }
            case Float4 : {
                return new Vector4(buffer);
            }
            case Ubyte4 : {
                return new Ubyte4(buffer);
            }
            case Ubyte4n : {
                return new Vector4(
                        buffer.get() / 255f,
                        buffer.get() / 255f,
                        buffer.get() / 255f,
                        buffer.get() / 255f
                );
            }
            case Half2 : {
                return new Vector2(
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort())
                );
            }
            case Half4 : {
                return new Vector4(
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort()),
                        HalfHelper.unpack(buffer.getShort())
                );
            }
            default : {
                throw new IllegalArgumentException();
            }
        }
    }
}
