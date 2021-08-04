package ffxiv.housim.saintcoinach.imaging;

import lombok.Getter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Header of an image file inside SqPack.
 */
public class ImageHeader {
    final static int LENGTH = 0x50;

    @Getter
    private ByteBuffer buffer;
    @Getter
    private ImageFormat format;
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    private int numMipmaps;
    @Getter
    private long endOfHeader;

    public ImageHeader(FileChannel channel) throws IOException {
        buffer = ByteBuffer.allocate(LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        channel.read(buffer);
        buffer.flip();

        buffer.getInt();// unknown
        int imageFormat = buffer.getShort();
        format = ImageFormat.of(imageFormat);
        buffer.getShort();// unknown
        width = buffer.getShort();
        height = buffer.getShort();
        buffer.getShort();// unknown
        numMipmaps = buffer.getShort();

    }
}
