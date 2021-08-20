package ffxiv.housim.saintcoinach.graphics.model;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class ModelBlock {
    int[] chunkDecompressedSizes = new int[ModelFile.PartsCount];// 44 bytes
    int[] chunkSizes = new int[ModelFile.PartsCount]; // 44 bytes
    int [] chunkOffsets = new int[ModelFile.PartsCount];// 44 bytes
    short[] chunkStartBlockIndex = new short[ModelFile.PartsCount];// 22 bytes
    short[] chunkNumBlocks = new short[ModelFile.PartsCount];// 22 bytes
    short numMeshes;// 2 bytes
    short numMaterials;// 2 bytes
    short y1;// 2 bytes
    short y2;// 2 bytes
    short[] blockSizes;// numBlocks * 2 bytes; Size of each data block below

    int numBlocks;// SUM(chunkNumBlocks)
    int[] blockOffsets;

    public ModelBlock(ByteBuffer buffer) {
        buffer.position(0x18);

        for (int i = 0; i < ModelFile.PartsCount; i++) {
            chunkDecompressedSizes[i] = buffer.getInt();
        }
        for (int i = 0; i < ModelFile.PartsCount; i++) {
            chunkSizes[i] = buffer.getInt();
        }
        for (int i = 0; i < ModelFile.PartsCount; i++) {
            chunkOffsets[i] = buffer.getInt();
        }
        for (int i = 0; i < ModelFile.PartsCount; i++) {
            chunkStartBlockIndex[i] = buffer.getShort();
        }
        for (int i = 0; i < ModelFile.PartsCount; i++) {
            chunkNumBlocks[i] = buffer.getShort();
            numBlocks += chunkNumBlocks[i];
        }
        numMeshes = buffer.getShort();
        numMaterials = buffer.getShort();
        y1 = buffer.getShort();
        y2 = buffer.getShort();

        // numBlocks = SUM( chunkNumBlocks )
        numBlocks = 0;
        for (int i = 0; i < ModelFile.PartsCount; i++) {
            numBlocks += chunkNumBlocks[i];
        }
        // numBlocks * 2 bytes
        blockSizes = new short[numBlocks];

        int offset = 0;
        blockOffsets = new int[numBlocks];
        for (int i = 0; i < numBlocks; i++) {
            blockSizes[i] = buffer.getShort();

            blockOffsets[i] = offset;
            offset += blockSizes[i];
        }

        log.debug("chunkDecompressedSize:{}\nchunkSizes:{}\nchunkOffsets:{}\nchunkStartBlockIndex:{}\nchunkNumBlocks:{}\nblockSizes:{}\nblockOffsets:{}", chunkDecompressedSizes, chunkSizes, chunkOffsets, chunkStartBlockIndex, chunkNumBlocks, blockSizes, blockOffsets);
        log.debug("numMeshes:{}, numMaterials:{}, numBlocks:{}, y1:{}, y2:{}", numMeshes, numMaterials, numBlocks, y1, y2);
    }

}
