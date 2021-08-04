package ffxiv.housim.saintcoinach.io;

import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackCollection {
    private Map<PackIdentifier, Pack> packs = new ConcurrentHashMap<>();

    private String dataDirectory;

    public Collection<Pack> getPacks() {
        return packs.values();
    }

    public PackCollection(@NonNull String dataDirectory) throws IOException {
        if (!new File(dataDirectory).exists()) {
            throw new FileNotFoundException("dataDirectory");
        }

        this.dataDirectory = dataDirectory;
    }

    public boolean filsExists(String path) {
        Pack pack = tryGetPack(path);
        if (pack != null) {
            return pack.fileExists(path);
        }
        return false;
    }

    public PackFile tryGetFile(String path) {
        Pack pack = tryGetPack(path);

        if (pack != null) {
            return pack.tryGetFile(path);
        }

        return null;
    }

    public Pack tryGetPack(String path) {
        PackIdentifier id = PackIdentifier.tryGet(path);
        if (id == null) {
            return null;
        }

        Pack pack = packs.get(id);
        if (pack != null) {
            return pack;
        }

        try {
            pack = new Pack(this, dataDirectory, id);
            packs.put(id, pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pack;
    }
}
