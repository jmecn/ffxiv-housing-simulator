package ffxiv.housim.saintcoinach.scene.terrain;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.scene.model.TransformedModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Terrain {

    final static int CountOffset = 0x04;
    final static int SizeOffset = 0x08;
    final static int BlockPositionsOffset = 0x34;
    final static int BlockPositionSize = 0x04;

    private PackFile file;
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

        for ( int i = 0; i < blockCount; i++) {

            String blockPath = blockDirectory + String.format("%s%04d.mdl", blockDirectory, i);
            ModelFile blockModelFile = (ModelFile) packs.tryGetFile(blockPath);

            buffer.position(BlockPositionsOffset + BlockPositionSize * i);
            float x = buffer.getShort();
            float y = 0;
            float z = buffer.getShort();

            Vector3 translation = new Vector3(blockSize * (x + 0.5f), 0, blockSize * (z + 0.5f));
            parts[i] = new TransformedModel(blockModelFile.getModelDefinition(), translation, Vector3.ZERO, Vector3.ONE);
        }

    }
}
