package ffxiv.housim.saintcoinach.io;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

@ToString
public class FileCommonHeader {
    final static int FileTypeOffset = 0x04;
    final static int FileLengthOffset = 0x10;
    final static int FileLengthShift = 7;

    @Getter
    private ByteBuffer buffer;

    @Getter
    private IIndexFile index;
    @Getter
    private int headerLength;
    @Getter
    private int fileType;
    @Getter
    private int fileLength;// uncompressed file length
    @Getter
    private int blockSize;
    @Getter
    private int length;
    @Getter
    private int blockCount;
    @Getter
    private long endOfHeader;

    public FileCommonHeader(@NonNull IIndexFile index, @NonNull FileChannel channel) throws IOException {
        this.index = index;
        read(channel);
    }

    private void read(FileChannel channel) throws IOException {
        ByteBuffer data = ByteBuffer.allocate(4);
        data.order(ByteOrder.LITTLE_ENDIAN);

        channel.read(data);
        data.flip();

        headerLength = data.getInt();

        buffer = ByteBuffer.allocate(headerLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(headerLength);

        channel.read(buffer);
        buffer.flip();

        //
        buffer.getInt();// 0x00 headerLength
        fileType = buffer.getInt();// 0x04 file type

        fileLength = buffer.getInt();// 0x08 uncompressed file size
        buffer.getInt();// 0x0C skip

        blockSize = buffer.getInt(); // 0x10
        blockCount = buffer.getInt(); // 0x14
        endOfHeader = channel.position();// 0x18

        length = blockSize << FileLengthShift;
    }
}