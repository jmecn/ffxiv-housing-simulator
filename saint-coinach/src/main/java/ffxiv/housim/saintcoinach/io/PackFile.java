package ffxiv.housim.saintcoinach.io;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Base class for files inside the SqPack.
 */
public abstract class PackFile {
    final static int Magic = 0x00000010;
    final static int HeaderLength = 0x10;
    final static int BlockPadding = 0x80;
    final static int CompressionThreshold = 0x7D00;

    @Getter
    @Setter
    private String path;

    @Getter
    private final Pack pack;

    @Getter
    protected FileCommonHeader commonHeader;

    private IIndexFile getIndex() {
        return commonHeader.getIndex();
    }

    public PackFile(Pack pack, FileCommonHeader commonHeader) {
        this.pack = pack;
        this.commonHeader = commonHeader;
    }

    public abstract byte[] getData();

    protected FileChannel getSourceStream() throws IOException {
        return this.pack.getDataStream(getIndex().getDatFile());
    }

    /**
     * Block:
     * 10h  Header
     * *    Data
     *
     * Header:
     * 4h   Magic
     * 4h   Unknown / Zero
     * 4h   Compressed data size
     * 4h   Raw data size
     * -> If compressed data size >= 7D00h then data is uncompressed
     */
    protected static void readBlock(FileChannel channel, ByteArrayOutputStream outStream) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(HeaderLength);
        header.order(ByteOrder.LITTLE_ENDIAN);
        channel.read(header);
        header.flip();

        int magic = header.getInt();
        header.getInt();// zero
        int compressedSize = header.getInt();
        int decompressedSize = header.getInt();

        if (Magic != magic) {
            throw new UnsupportedOperationException("Magic number not present (-> don't know how to continue).");
        }
        boolean isCompressed = compressedSize < CompressionThreshold;
        int blockSize = isCompressed ? compressedSize : decompressedSize;

        // An uncompressed block in an ScdOggFile was corrupted due to this
        // extra padding injecting extra 0s into the output stream.  I'm
        // not certain padding is only applied to compressed blocks.  The
        // padding algorithm may require more work if additional problems
        // are found.
        if (isCompressed && (blockSize + HeaderLength) % BlockPadding != 0) {
            // Add padding if necessary
            blockSize += BlockPadding - ((blockSize + HeaderLength) % BlockPadding);
        }

        byte[] compressed = new byte[blockSize];
        ByteBuffer buffer = ByteBuffer.wrap(compressed);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        channel.read(buffer);
        buffer.flip();

        if (isCompressed) {
            byte[] decompressed = new byte[decompressedSize];
            inflate(compressed, decompressed);
            outStream.write(decompressed);
        } else {
            outStream.write(compressed);
        }
    }

    protected static void inflate(byte[] compressed, byte[] decompressed) {
        // Build the zlib header stuff
        byte[] gzipedData = new byte[compressed.length + 6]; // Need to add 6 bytes
        // for missing zlib
        // header/footer
        // Zlib Magic Number
        gzipedData[0] = (byte) 0x78;
        gzipedData[1] = (byte) 0x9C;

        // Actual Data
        System.arraycopy(compressed, 0, gzipedData, 2, compressed.length);

        // Checksum
        int checksum = adler32(compressed);
        byte[] checksumByte = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(checksum).array();
        System.arraycopy(checksumByte, 0, gzipedData, 2 + compressed.length, 4);

        //Decompress this block
        try {

            Inflater inflater = new Inflater();

            inflater.setInput(gzipedData);
            inflater.setOutput(decompressed);

            int err = inflater.init();
            CHECK_ERR(inflater, err, "inflateInit");

            while (inflater.total_out < decompressed.length
                    && inflater.total_in < gzipedData.length) {
                inflater.avail_in = inflater.avail_out = 1;// force small buffers
                err = inflater.inflate(JZlib.Z_NO_FLUSH);
                if (err == JZlib.Z_STREAM_END)
                    break;
                CHECK_ERR(inflater, err, "inflate");
            }

            err = inflater.end();
            CHECK_ERR(inflater, err, "inflateEnd");

            inflater.finished();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void CHECK_ERR(Inflater z, int err, String msg) {
        if (err != JZlib.Z_OK) {
            if (z.msg != null)
                System.out.print(z.msg + " ");
            System.out.println(msg + " error: " + err);
            System.exit(1);
        }
    }

    private static int adler32(byte[] bytes) {
        final int a32mod = 65521;
        int s1 = 1, s2 = 0;

        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];

            s1 = (s1 + b) % a32mod;
            s2 = (s2 + s1) % a32mod;
        }
        return (s2 << 16) + s1;
    }
}
