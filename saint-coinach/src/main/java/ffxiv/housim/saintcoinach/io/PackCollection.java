package ffxiv.housim.saintcoinach.io;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PackCollection {
    private Map<PackIdentifier, Pack> packs = new ConcurrentHashMap<>();

    private String dataDirectory;

    public Collection<Pack> getPacks() {
        return packs.values();
    }

    public PackCollection(@NonNull String dataDirectory) {
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

    public Pack tryGetPack(PackIdentifier id) {
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
