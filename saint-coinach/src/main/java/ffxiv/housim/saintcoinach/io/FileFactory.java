package ffxiv.housim.saintcoinach.io;

import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;

import java.io.IOException;
import java.nio.channels.FileChannel;

public final class FileFactory {

    public static PackFile get(Pack pack, IIndexFile indexFile) {
        FileChannel channel = pack.getDataStream(indexFile.getDatFile());

        FileCommonHeader header = null;
        try {
            channel.position(indexFile.getOffset());
            header = new FileCommonHeader(indexFile, channel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        switch (header.getFileType()) {
            case Empty:
                return new EmptyFile(pack, header);
            case Binary:
                return new BinaryFile(pack, header);
            case Model:
                return new ModelFile(pack, header);
            case Image:
                return new ImageFile(pack, header);
            default :
                throw new UnsupportedOperationException("Unknown file type " + header.getFileType());
        }
    }
}
