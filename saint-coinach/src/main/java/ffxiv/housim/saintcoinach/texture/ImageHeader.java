package ffxiv.housim.saintcoinach.texture;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Header of an image file inside SqPack.
 */
@ToString
@Slf4j
public class ImageHeader {
    final static int LENGTH = 0x50;
    final static int MIPMAP_OFFSET = 0x1C;

    @Getter
    private final ByteBuffer buffer;
    @Getter
    private final ImageFormat format;
    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final int depth;
    @Getter
    private final int numMipmaps;
    @Getter
    private final int[] mipmapOffsets;

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
        depth = buffer.getShort();// unknown
        numMipmaps = buffer.getShort();

        mipmapOffsets = new int[numMipmaps];

        buffer.position(MIPMAP_OFFSET);
        for (int i=0; i<numMipmaps; i++) {
            mipmapOffsets[i] = buffer.getInt() - LENGTH;
        }

        log.info("format:{}, width:{}, height:{}, depth:{}, numMipmaps:{}, mipmapOffsets:{}", format, width, height, depth, numMipmaps, mipmapOffsets);
    }
}
