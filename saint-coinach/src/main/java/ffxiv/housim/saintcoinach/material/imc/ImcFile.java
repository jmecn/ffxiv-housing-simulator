package ffxiv.housim.saintcoinach.material.imc;

import ffxiv.housim.saintcoinach.io.PackFile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class ImcFile {

    private final Map<Byte, ImcPart> parts = new HashMap<>();

    private final PackFile sourceFile;

    private final List<ImcPart> partList = new ArrayList<>(8);

    private final short count;

    private final short partsMask;

    public ImcFile(PackFile sourceFile) {
        this.sourceFile = sourceFile;

        byte[] data = sourceFile.getData();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        count = buffer.getShort();
        partsMask = buffer.getShort();

        for (byte bit = 0; bit < 8; ++bit) {
            int match = 1 << bit;
            if ((partsMask & match) == match) {
                ImcPart part = new ImcPart(buffer, bit);
                partList.add(part);
                parts.put(bit, part);
            }
        }

        short rem = count;
        while (--rem >= 0) {
            for (var part : parts.values()) {
                part.getVariants().add(new ImcVariant(buffer));
            }
        }
    }

    public ImcVariant getVariant(int index) {
        return partList.get(0).getVariant(index);
    }

    public ImcVariant getVariant(byte partKey, int index) {
        return parts.get(partKey).getVariant(index);
    }
}
