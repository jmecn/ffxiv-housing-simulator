package ffxiv.housim.saintcoinach.graphics.pcb;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

// TODO: This class should be reworked and checked for errors - the problems they cause with certain PCBs are meanwhile caught in LgbModelEntry L55+
@Slf4j
public class PcbFile {

    int entryCount;
    int indicesCount;

    private PackFile file;

    private List<PcbBlockEntry> data;

    public PcbFile(PackFile file) {
        this.file = file;

        byte[] bytes = file.getData();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int magic = buffer.getInt();
        int type = buffer.getInt();
        entryCount = buffer.getInt();
        indicesCount = buffer.getInt();
        long padding = buffer.getLong();// paddings

        log.info("magic:{}, type:{}, data.length:{}, entryCount:{}, indicesCount:{}", String.format("%08X", magic), type, bytes.length, entryCount, indicesCount);

        data = new ArrayList<>(entryCount);

        int offset = buffer.position();
        PcbBlockEntry entry = new PcbBlockEntry(this, buffer, offset);

        offset += entry.getBlockSize();
    }
}
