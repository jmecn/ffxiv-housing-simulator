package ffxiv.housim.saintcoinach.graphics.shpk;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Parameter {
    @Getter
    private ParameterType type;

    @Getter
    private int id;

    @Getter
    private short registerIndex;
    @Getter
    private short registerCount;

    @Getter
    private String name;

    Parameter(ParameterType type, ByteBuffer buffer, int parameterListOffset) {
        this.type = type;

        this.id = buffer.getInt();
        int nameOffset = buffer.getInt();
        int nameLength = buffer.getInt();

        this.registerIndex = buffer.getShort();
        this.registerCount = buffer.getShort();

        int offset = parameterListOffset + nameOffset;
        int length = nameLength;

        byte[] bytes = new byte[length];
        buffer.get(offset, bytes);
        this.name = new String(bytes, StandardCharsets.US_ASCII);
    }

    @Override
    public String toString() {
        return type + ": " + name;
    }
}
