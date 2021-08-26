package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class IndexSource implements IPackSource {

    private final Map<Integer, PackDirectory> directories = new HashMap<>();
    private final Map<String, Integer> directoryPathMap = new HashMap<>();
    @Getter
    private final Pack pack;
    @Getter
    private final Index index;

    public IndexSource(Pack pack, Index index) {
        this.pack = pack;
        this.index = index;
    }

    @Override
    public boolean directoryExists(String path) {
        int hash = Hash.compute(path);
        return directoryExists(hash);
    }

    @Override
    public boolean directoryExists(int hash) {
        return index.getDirectories().containsKey(hash);
    }

    public PackDirectory getDirectory(String path) {
        int hash = Hash.compute(path);
        return getDirectory(hash);
    }

    public PackDirectory getDirectory(int hash) {
        PackDirectory dir = directories.get(hash);
        if (dir != null) {
            return dir;
        }

        IndexDirectory idxDir = index.getDirectories().get(hash);
        dir = new PackDirectory(pack, idxDir);
        directories.put(hash, dir);
        return dir;
    }

    @Override
    public PackDirectory tryGetDirectory(String path) {
        int hash = Hash.compute(path);
        return tryGetDirectory(hash);
    }

    @Override
    public PackDirectory tryGetDirectory(int hash) {
        PackDirectory dir = directories.get(hash);
        if (dir != null) {
            return dir;
        }

        IndexDirectory idxDir = index.getDirectories().get(hash);
        if (idxDir != null) {
            dir = new PackDirectory(pack, idxDir);
            directories.put(hash, dir);
            return dir;
        }

        return null;
    }

    @Override
    public boolean fileExists(String path) {
        int lastSeperator = path.lastIndexOf("/");
        if (lastSeperator < 0) {
            throw new IllegalArgumentException();
        }

        String dirPath = path.substring(0, lastSeperator);
        String baseName = path.substring(lastSeperator + 1);

        PackDirectory dir = tryGetDirectory(dirPath);
        if (dir != null) {
            return dir.fileExists(baseName);
        }

        return false;
    }

    @Override
    public PackFile tryGetFile(String path) {
        int lastSeperator = path.lastIndexOf("/");
        if (lastSeperator < 0) {
            throw new IllegalArgumentException();
        }

        String dirPath = path.substring(0, lastSeperator);
        String baseName = path.substring(lastSeperator + 1);

        PackDirectory dir = tryGetDirectory(dirPath);
        if (dir != null) {
            return dir.tryGetFile(baseName);
        }

        return null;
    }

    @Override
    public PackFile tryGetFile(int directoryKey, int fileKey) {
        PackDirectory dir = tryGetDirectory(directoryKey);
        if (dir != null) {
            return dir.tryGetFile(fileKey);
        }

        return null;
    }

}