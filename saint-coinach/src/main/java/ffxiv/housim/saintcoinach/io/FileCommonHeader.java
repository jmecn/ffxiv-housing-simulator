package ffxiv.housim.saintcoinach.io;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

@ToString
@Slf4j
public class FileCommonHeader {

    @Getter
    private final ByteBuffer buffer;

    @Getter
    private final IIndexFile index;
    @Getter
    private final int headerLength;
    @Getter
    private final FileType fileType;
    @Getter
    private final int fileLength;// uncompressed file length
    @Getter
    private final int blockSize;
    @Getter
    private final int length;
    @Getter
    private final int blockCount;
    @Getter
    private final long endOfHeader;

    public FileCommonHeader(@NonNull IIndexFile index, @NonNull FileChannel channel) throws IOException {
        this.index = index;

        ByteBuffer data = ByteBuffer.allocate(4);
        data.order(ByteOrder.LITTLE_ENDIAN);

        channel.read(data);
        data.flip();

        headerLength = data.getInt();
        if (headerLength == 0) {
            log.warn("header length is 0!");
            throw new IllegalArgumentException("Header length is 0!");
        } else {
            buffer = ByteBuffer.allocate(headerLength);
        }

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(headerLength);
        channel.read(buffer);
        buffer.flip();

        //
        buffer.getInt();// 0x00 headerLength
        int type = buffer.getInt();// 0x04 file type
        fileType = FileType.of(type);

        fileLength = buffer.getInt();// 0x08 uncompressed file size
        buffer.getInt();// 0x0C skip

        blockSize = buffer.getInt(); // 0x10
        blockCount = buffer.getInt(); // 0x14
        endOfHeader = channel.position();// 0x18

        length = blockSize * PackFile.BlockPadding;
        log.debug("headerLength:{}, fileType:{}, fileLength:{}, blockSize:{}, blockCount:{}", headerLength, fileType, fileLength, blockSize, blockCount);
    }

}