package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.io.FileCommonHeader;
import ffxiv.housim.saintcoinach.io.Pack;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;

/**
 * Model file stored inside SqPack.
 */
@Slf4j
public class ModelFile extends PackFile {

    final static int PartsCount = 0x0B;

    @SuppressWarnings("unchecked")
    private final WeakReference<byte[]>[] partsCache = new WeakReference[PartsCount];
    private WeakReference<byte[]> combinedCache;
    private WeakReference<ModelDefinition> defCache;

    private final ModelBlock modelBlock;

    public ModelFile(Pack pack, FileCommonHeader commonHeader) {
        super(pack, commonHeader);

        modelBlock = new ModelBlock(commonHeader.getBuffer());
    }

    public ModelDefinition getModelDefinition() {
        if (defCache != null && defCache.get() != null) {
            return defCache.get();
        }

        ModelDefinition def = new ModelDefinition(this);
        defCache = new WeakReference<>(def);

        return def;
    }

    @Override
    public byte[] getData() {
        if (combinedCache != null && combinedCache.get() != null) {
            return combinedCache.get();
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        for (int i = 0; i < PartsCount; i++) {
            try {
                byte[] part = getPart(i);
                outStream.write(part, 0, part.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] data = outStream.toByteArray();
        log.info("data length:{}", data.length);

        combinedCache = new WeakReference<>(data);

        return data;
    }

    public byte[] getPart(int part) throws IOException {
        if (part >= PartsCount) {
            throw new ArrayIndexOutOfBoundsException(part);
        }

        if (partsCache[part] != null && partsCache[part].get() != null) {
            return partsCache[part].get();
        }

        byte[] data = readPart(part);

        partsCache[part] = new WeakReference<>(data);

        return data;
    }

    final static int MinimumBlockOffset = 0x9C;
    final static int BlockCountOffset = 0xB2;

    private byte[] readPart(int part) throws IOException {

        int blockStart = modelBlock.chunkStartBlockIndex[part];
        int blockCount = modelBlock.chunkNumBlocks[part];
        log.debug("read part:{}, blockStart:{}, blockCount:{}", part, blockStart, blockCount);

        FileChannel channel = getSourceStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(0x80);

        for (int i = 0; i < blockCount; i++) {
            channel.position(commonHeader.getEndOfHeader() + modelBlock.blockOffsets[blockStart + i]);
            readBlock(channel, outStream);
        }

        return outStream.toByteArray();
    }
}
