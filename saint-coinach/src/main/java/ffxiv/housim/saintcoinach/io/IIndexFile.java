package ffxiv.housim.saintcoinach.io;

public interface IIndexFile {
    PackIdentifier getPackId();

    int getFileKey();

    int getOffset();

    int getDatFile();
}