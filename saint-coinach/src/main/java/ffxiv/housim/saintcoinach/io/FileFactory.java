package ffxiv.housim.saintcoinach.io;

import ffxiv.housim.saintcoinach.imaging.ImageFile;

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
        }

        switch (header.getFileType()) {
            case FileType.Empty:
                return new EmptyFile(pack, header);
            case FileType.Binary:
                return new BinaryFile(pack, header);
            case FileType.Model:
                break;
            case FileType.Image:
                return new ImageFile(pack, header);
            default :
                throw new UnsupportedOperationException("Unknown file type " + header.getFileType());
        }

        return null;
    }
}
