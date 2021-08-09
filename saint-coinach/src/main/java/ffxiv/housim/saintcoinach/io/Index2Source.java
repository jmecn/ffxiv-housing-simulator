package ffxiv.housim.saintcoinach.io;

import lombok.Getter;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class Index2Source implements IPackSource {

    private Map<Integer, WeakReference<PackFile>> files = new HashMap<>();
    private Map<String, Integer> filePathMap = new HashMap<>();
    @Getter
    private Pack pack;
    @Getter
    private Index2 index;

    public Index2Source(Pack pack, Index2 index) {
        this.pack = pack;
        this.index = index;
    }

    public boolean fileExists(String path) {
        int hash = Hash.compute(path);
        return fileExists(hash);
    }

    public boolean fileExists(int hash) {
        return index.getFiles().containsKey(hash);
    }

    public PackFile getFile(String path) {
        int hash = Hash.compute(path);
        return getFile(hash);
    }

    public PackFile getFile(int hash) {
        WeakReference<PackFile> fileRef = files.get(hash);
        if (fileRef != null && fileRef.get() != null) {
            return fileRef.get();
        }

        Index2File idxFile = index.getFiles().get(hash);
        PackFile file = FileFactory.get(pack, idxFile);
        files.put(hash, new WeakReference<>(file));
        return file;
    }

    @Override
    public PackFile tryGetFile(String path) {
        int hash = Hash.compute(path);
        return tryGetFile(hash);
    }

    public PackFile tryGetFile(int hash) {
        WeakReference<PackFile> fileRef = files.get(hash);
        if (fileRef != null && fileRef.get() != null) {
            return fileRef.get();
        }

        Index2File idxFile = index.getFiles().get(hash);
        if (idxFile != null) {
            PackFile file = FileFactory.get(pack, idxFile);
            files.put(hash, new WeakReference<>(file));
        }

        return null;
    }
}