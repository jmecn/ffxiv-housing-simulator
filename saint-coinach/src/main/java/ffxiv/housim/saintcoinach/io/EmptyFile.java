package ffxiv.housim.saintcoinach.io;

/**
 * a placeholder
 */
public class EmptyFile extends PackFile {
    public EmptyFile(Pack pack, FileCommonHeader header) {
        super(pack, header);
    }

    public byte[] getData() {
        return new byte[0];
    }
}
