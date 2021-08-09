package ffxiv.housim.saintcoinach.graphics;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class VertexFormat {
    @Getter
    private VertexFormatElement[] elements;

    VertexFormat(ByteBuffer buffer) {
        List<VertexFormatElement> elements = new ArrayList<>();

        int offset = buffer.position();

        int o = offset;
        while((buffer.get() & 0xFF) != 0xFF) {
            buffer.position(o);
            elements.add(new VertexFormatElement(buffer));
            o = buffer.position();
        }

        this.elements = elements.toArray(new VertexFormatElement[0]);
        buffer.position(offset + 0x88);
    }
}