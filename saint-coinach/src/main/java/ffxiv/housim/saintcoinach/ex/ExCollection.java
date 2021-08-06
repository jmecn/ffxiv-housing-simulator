package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExCollection {
    private Map<Integer, String> sheetIdentifiers = new HashMap<>();
    private Map<String, WeakReference<ISheet>> sheets = new ConcurrentHashMap<>();
    private Set<String> availableSheetSet = new HashSet<>();

    @Getter
    private PackCollection packCollection;
    @Getter
    private Language activeLanguage;
    @Getter
    private String activeLanguageCode;
    @Getter
    private List<String> availableSheets;

    public ExCollection(PackCollection packCollection) {
        this.packCollection = packCollection;
        buildIndex();
    }
    private void buildIndex() {
        PackFile exRoot = packCollection.tryGetFile("exd/root.exl");
        List<String> available = new ArrayList<>();

        ByteArrayInputStream in = new ByteArrayInputStream(exRoot.getData());

        Scanner scanner = new Scanner(in, StandardCharsets.US_ASCII.name());
        scanner.nextLine();// EXLT,2
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == null || line.isEmpty()) {
                continue;
            }
            String[] split = line.split(",");
            if (split.length != 2) {
                continue;
            }

            String name = split[0].toLowerCase();
            Integer id = Integer.parseInt(split[1]);

            available.add(name);
            availableSheetSet.add(name);
            if (id >= 0) {
                sheetIdentifiers.put(id, name);
                log.info("id:{}, name:{}", id, name);
            }
        }
    }

    public boolean sheetExists(int id) {
        return sheetIdentifiers.containsKey(id);
    }

    public boolean sheetExists(String name) {
        return availableSheetSet.contains(name);
    }

    public ISheet getSheet(int id) {
        String name = sheetIdentifiers.get(id);
        return getSheet(name);
    }

    public ISheet getSheet(String name) {
        WeakReference<ISheet> sheetRef = sheets.get(name);
        if (sheetRef != null && sheetRef.get() != null) {
            return sheetRef.get();
        }

        if (!availableSheetSet.contains(name)) {
            throw new IllegalArgumentException("Unknown sheet " + name);
        }

        String exhPath = String.format("exd/%s.exh", name);
        PackFile exh = packCollection.tryGetFile(exhPath);

        Header header = createHeader(name, exh);
        ISheet sheet = createSheet(header);
        return sheet;
    }

    protected Header createHeader(String name, PackFile file) {
        return new Header(this, name, file);
    }

    protected ISheet createSheet(Header header) {
        if (header.getVariant() == 1) {
            // TODO
        }
        // TODO
        return null;
    }
}
