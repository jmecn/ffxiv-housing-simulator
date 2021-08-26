package ffxiv.housim.saintcoinach.io;

public interface IPackSource {

    boolean directoryExists(String path);

    boolean directoryExists(int hash);

    PackDirectory tryGetDirectory(String path);

    PackDirectory tryGetDirectory(int hash);

    boolean fileExists(String path);

    PackFile tryGetFile(String name);

    PackFile tryGetFile(int directoryKey, int fileKey);
}