package ffxiv.housim.saintcoinach.scene.pcb;

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

        int unkonwn1 = buffer.getInt();
        int unkonwn2 = buffer.getInt();
        entryCount = buffer.getInt();
        indicesCount = buffer.getInt();
        long padding = buffer.getLong();// paddings

        log.debug("unkonwn1:{}, unkonwn2:{}, data.length:{}, entryCount:{}, indicesCount:{}", unkonwn1, unkonwn2, bytes.length, entryCount, indicesCount);

        data = new ArrayList<>(entryCount);

        int offset = buffer.position();
        PcbBlockEntry entry = new PcbBlockEntry(this, buffer, offset);

        offset += entry.getBlockSize();
    }
}
