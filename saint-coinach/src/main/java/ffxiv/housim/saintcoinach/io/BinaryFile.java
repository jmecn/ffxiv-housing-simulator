package ffxiv.housim.saintcoinach.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class BinaryFile extends PackFile {

    private WeakReference<byte[]> bufferCache;

    private List<BinaryBlock> blockTable;

    public BinaryFile(Pack pack, FileCommonHeader commonHeader) {
        super(pack, commonHeader);
    }

    @Override
    public byte[] getData() {
        if (bufferCache != null && bufferCache.get() != null) {
            return bufferCache.get();
        }

        byte[] buffer = new byte[0];
        try {
            buffer = read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        assert buffer.length == commonHeader.getFileLength();

        bufferCache = new WeakReference<>(buffer);

        return buffer;
    }

    private byte[] read() throws IOException {
        FileChannel channel = getSourceStream();

        readBlockTable();

        int length = commonHeader.getFileLength();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(length);

        for (BinaryBlock block : blockTable) {
            channel.position(commonHeader.getEndOfHeader() + block.offset);
            readBlock(channel, outStream);
        }

        return outStream.toByteArray();
    }

    private void readBlockTable() {
        ByteBuffer buffer = commonHeader.getBuffer();
        int blockCount = commonHeader.getBlockCount();
        buffer.position(0x18);

        blockTable = new ArrayList<>(blockCount);
        for (int i=0; i<blockCount; i++) {
            BinaryBlock block = new BinaryBlock();
            block.offset = buffer.getInt();
            block.blockSize = buffer.getShort();
            block.depressedDataSize = buffer.getShort();
            blockTable.add(block);
        }
    }

}
