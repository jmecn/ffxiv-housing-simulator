package ffxiv.housim.saintcoinach.io;

public interface IPackSource {

    boolean fileExists(String path);

    PackFile tryGetFile(String name);

}