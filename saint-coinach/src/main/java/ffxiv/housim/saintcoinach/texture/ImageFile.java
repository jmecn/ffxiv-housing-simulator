package ffxiv.housim.saintcoinach.texture;

import ffxiv.housim.saintcoinach.io.FileCommonHeader;
import ffxiv.housim.saintcoinach.io.Pack;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ImageFile extends PackFile {

    @Getter
    @Setter
    private int id;

    private WeakReference<byte[]> bufferCache;

    @Getter
    private ImageHeader imageHeader;

    private TexBlock[] blockTable;

    public ImageFile(Pack pack, FileCommonHeader commonHeader) {
        super(pack, commonHeader);
        try {
            FileChannel channel = getSourceStream();
            // read image header
            channel.position(commonHeader.getEndOfHeader());
            imageHeader = new ImageHeader(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return imageHeader.getWidth();
    }

    public int getHeight() {
        return imageHeader.getHeight();
    }

    public ImageFormat getFormat() {
        return imageHeader.getFormat();
    }

    @Override
    public byte[] getData() {
        if (bufferCache != null && bufferCache.get() != null) {
            return bufferCache.get();
        }

        byte[] buffer;
        try {
            buffer = read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        assert (buffer.length + ImageHeader.LENGTH) == commonHeader.getFileLength();

        bufferCache = new WeakReference<>(buffer);

        return buffer;
    }

    private byte[] read() throws IOException {
        FileChannel channel = getSourceStream();

        readBlockTable();

        int length = commonHeader.getFileLength();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(length);

        int start = commonHeader.getIndex().getOffset() + commonHeader.getHeaderLength();
        for (TexBlock block : blockTable) {
            int offset = start + block.offset;
            channel.position(offset);
            for (int subBlockSize : block.subBlockSize) {
                channel.position(offset);
                try {
                    readBlock(channel, outStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                offset += subBlockSize;
            }
        }

        return outStream.toByteArray();
    }

    private void readBlockTable() {

        ByteBuffer buffer = commonHeader.getBuffer();
        int blockCount = commonHeader.getBlockCount();
        blockTable = new TexBlock[blockCount];

        buffer.position(0x18);

        // Texture block table
        for (int i = 0; i < blockCount; i++) {
            TexBlock block = new TexBlock();
            block.offset = buffer.getInt();
            block.blockSize = buffer.getInt();
            block.depressedDataSize = buffer.getInt();
            block.blockTableOffset = buffer.getInt();
            block.numSubBlocks = buffer.getInt();
            block.subBlockSize = new int[block.numSubBlocks];

            blockTable[i] = block;
        }

        for (int i=0; i<blockCount; i++) {
            TexBlock block = blockTable[i];
            int sumBlockSize = 0;
            for (int j = 0; j < block.numSubBlocks; j++) {
                int blockSize = buffer.getShort();
                block.subBlockSize[j] = blockSize;
                sumBlockSize += blockSize;
            }
            assert block.blockSize == sumBlockSize;
        }
    }

}