package ffxiv.housim.saintcoinach.io;

import lombok.Getter;
import lombok.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Directory inside a SqPack
 */
public class PackDirectory implements IPackSource {

    private Map<String, Integer> fileNameMap = new HashMap<>();
    private Map<Integer, WeakReference<PackFile>> files = new ConcurrentHashMap<>();
    private String path;
    @Getter
    private Pack pack;
    @Getter
    private IndexDirectory index;

    public PackDirectory(@NonNull Pack pack, @NonNull IndexDirectory index) {
        this.pack = pack;
        this.index = index;
    }

    public String getPath() {
        if (path == null) {
            return String.format("%s/%08X", pack.toString(), index.getKey());
        } else {
            return path;
        }
    }

    @Override
    public String toString() {
        return getPath();
    }

    @Override
    public boolean directoryExists(String path) {
        return false;
    }

    @Override
    public boolean directoryExists(int hash) {
        return false;
    }

    @Override
    public PackDirectory tryGetDirectory(String path) {
        return null;
    }

    @Override
    public PackDirectory tryGetDirectory(int hash) {
        return null;
    }

    @Override
    public boolean fileExists(String name) {
        int hash = Hash.compute(name);
        return fileExists(hash);
    }

    public boolean fileExists(int hash) {
        return index.getFiles().containsKey(hash);
    }

    @Override
    public PackFile tryGetFile(String name) {
        int hash = Hash.compute(name);
        PackFile file = tryGetFile(hash);
        if (file != null) {
            file.setPath(String.format("%s/%s", getPath(), name));
        }
        return file;
    }

    @Override
    public PackFile tryGetFile(int directoryKey, int fileKey) {
        return tryGetFile(fileKey);
    }

    public PackFile tryGetFile(int hash) {
        WeakReference<PackFile> fileRef = files.get(hash);
        if (fileRef != null && fileRef.get() != null) {
            return fileRef.get();
        }

        IndexFile indexFile = index.getFiles().get(hash);
        if (indexFile == null) {
            return null;
        }

        PackFile file = FileFactory.get(pack, indexFile);

        files.put(hash, new WeakReference<>(file));
        return file;
    }
}
