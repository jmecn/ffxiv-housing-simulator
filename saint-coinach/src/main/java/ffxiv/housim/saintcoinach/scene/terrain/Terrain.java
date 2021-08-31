package ffxiv.housim.saintcoinach.scene.terrain;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.model.TransformedModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class Terrain {

    final static int BlockPositionsOffset = 0x34;
    final static int BlockPositionSize = 0x04;

    private PackFile file;
    @Getter
    private TransformedModel[] parts;

    public Terrain(PackFile file) {
        byte[] bytes = file.getData();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int magic = buffer.getInt();
        int blockCount = buffer.getInt();
        int blockSize = buffer.getInt();

        String path = file.getPath();
        String blockDirectory = path.substring(0, path.lastIndexOf("/") + 1);

        PackCollection packs = file.getPack().getCollection();

        parts = new TransformedModel[blockCount];

        log.info("blockCount:{}, blockSize:{}, basePath:{}", blockCount, blockSize, blockDirectory);
        for ( int i = 0; i < blockCount; i++) {
            String blockPath = String.format("%s%04d.mdl", blockDirectory, i);
            ModelFile mdl = (ModelFile) packs.tryGetFile(blockPath);

            buffer.position(BlockPositionsOffset + BlockPositionSize * i);
            float x = buffer.getShort();
            float y = 0;
            float z = buffer.getShort();

            Vector3 translation = new Vector3(blockSize * (x + 0.5f), 0, blockSize * (z + 0.5f));

            if (mdl == null) {
                log.warn("terrain part#{} id missing, path={}", i, blockPath);
                continue;
            }
            parts[i] = new TransformedModel(mdl.getModelDefinition(), translation, Vector3.ZERO, Vector3.ONE);
        }

    }
}
