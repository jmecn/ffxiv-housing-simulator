package ffxiv.housim.saintcoinach.imaging;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class GraphicsFileTextureDefinition {
    @Getter
    private Map<Short, GftdEntry> entries = new HashMap<>();
    @Getter
    private PackFile file;

    public GraphicsFileTextureDefinition(PackFile file) {
        byte[] data = file.getData();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.flip();

        long magic = buffer.getLong();
        if (magic != 0x3030313064746667L) {// gftd0100
            throw new IllegalArgumentException();
        }

        int count = buffer.getInt();
        while(--count >= 0) {

            GftdEntry entry = new GftdEntry();
            entry.id = buffer.getShort();
            entry.x = buffer.getShort();
            entry.y = buffer.getShort();
            entry.width = buffer.getShort();
            entry.height = buffer.getShort();
            entry.offsetX = buffer.getShort();
            entry.offsetY = buffer.getShort();
            entry.padding = buffer.getShort();

            entries.put(entry.id, entry);
        }
    }

}
