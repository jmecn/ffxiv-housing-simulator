package ffxiv.housim.saintcoinach.io;

import ffxiv.housim.saintcoinach.utils.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Pack {

    private final static String IndexFileFormat = "%02x%02x%02x.win32.index";
    private final static String Index2FileFormat = "%02x%02x%02x.win32.index2";
    private final static String DatFileFormat = "%02x%02x%02x.win32.dat%d";

    private final Map<Pair<Thread, Integer>, WeakReference<FileChannel>> dataStreams = new HashMap<>();

    private PackIdentifier id;
    @Getter
    private PackCollection collection;
    private String dataDirectory;
    @Getter
    private IPackSource source;

    // Serialize only
    public Pack() {
    }

    public Pack(String dataDirectory, PackIdentifier id) throws IOException {
        this(null, dataDirectory, id);
    }

    public Pack(PackCollection collection, @NonNull String dataDirectory, @NonNull PackIdentifier id) throws IOException {

        this.collection = collection;
        this.dataDirectory = dataDirectory;
        this.id = id;

        Path indexPath = Paths.get(dataDirectory, id.getExpansion(),//
                String.format(IndexFileFormat, id.getTypeKey(), id.getExpansionKey(), id.getNumber()));

        Path index2Path = Paths.get(dataDirectory, id.getExpansion(),//
                String.format(Index2FileFormat, id.getTypeKey(), id.getExpansionKey(), id.getNumber()));

        if (Files.exists(indexPath)) {
            source = new IndexSource(this, new Index(id, new FileInputStream(indexPath.toFile())));
        } else
        if (Files.exists(index2Path)) {
            source = new Index2Source(this, new Index2(id, new FileInputStream(index2Path.toFile())));
        } else {
            log.warn("Index not found: {}", id);
        }

    }

    public FileChannel getDataStream(int datFile) {
        Thread thread = Thread.currentThread();

        FileChannel channel = null;

        // get stream from cache
        Pair<Thread, Integer> key = new Pair<>(thread, datFile);
        WeakReference<FileChannel> streamRef;

        synchronized (dataStreams) {
            streamRef = dataStreams.get(key);
        }

        if (streamRef != null) {
            channel = streamRef.get();
        }

        if (channel != null && channel.isOpen()) {
            return channel;
        }

        // read from file system
        String baseName = String.format(DatFileFormat, id.getTypeKey(), id.getExpansionKey(), id.getNumber(), datFile);
        Path fullPath = Paths.get(dataDirectory, id.getExpansion(), baseName);

        try {
            channel = new FileInputStream(fullPath.toFile()).getChannel();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // cache
        synchronized (dataStreams) {
            dataStreams.put(key, new WeakReference<>(channel));
        }

        return channel;
    }

    public boolean fileExists(String path) {
        return source.fileExists(path);
    }

    public PackFile tryGetFile(String path) {
        return source.tryGetFile(path);
    }

    @Override
    public String toString() {
        return String.format("%s/%02x%02x%02x", id.getExpansion(), id.getTypeKey(), id.getExpansionKey(), id.getNumber());
    }
}
