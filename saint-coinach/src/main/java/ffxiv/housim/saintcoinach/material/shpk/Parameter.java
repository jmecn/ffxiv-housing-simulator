package ffxiv.housim.saintcoinach.material.shpk;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

public class Parameter {

    @Getter
    private ParameterType type;

    @Getter
    private int id;

    @Getter
    private int nameOffset;

    @Getter
    private int nameLength;

    @Getter
    private short registerIndex;
    @Getter
    private short registerCount;

    @Getter
    @Setter
    private String name;

    Parameter(ParameterType type, ByteBuffer buffer, int parameterListOffset) {
        this.type = type;

        this.id = buffer.getInt();
        this.nameOffset = buffer.getInt();
        this.nameLength = buffer.getInt();

        this.registerIndex = buffer.getShort();
        this.registerCount = buffer.getShort();
    }

    @Override
    public String toString() {
        return String.format("0x%08X: %s (%s)", id, name, type);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
