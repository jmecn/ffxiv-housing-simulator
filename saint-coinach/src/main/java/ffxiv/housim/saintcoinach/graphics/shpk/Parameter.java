package ffxiv.housim.saintcoinach.graphics.shpk;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Parameter {

    @Getter
    private ParameterHeader header;
    @Getter
    private ParameterType type;
    @Getter
    private int id;
    @Getter
    private String name;

    public Parameter(ShPkFile file, ParameterHeader header, ByteBuffer buffer) {
        this.header = header;
        this.type = header.getType();
        this.id = header.getId();

        int offset = file.getHeader().getParameterListOffset() + header.getNameOffset();
        int length = header.getNameLength();

        byte[] bytes = new byte[length];
        buffer.get(offset, bytes);
        this.name = new String(bytes, StandardCharsets.US_ASCII);
    }
}
