package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DataSheet<T extends IDataRow> implements IDataSheet<T> {

    private boolean partialSheetsCreated = false;
    protected final Class<T> dataRowClass;

    private final Map<Page, ISheet<T>> partialSheets = new HashMap<>();
    private final Map<Integer, ISheet<T>> rowToPartialSheetMap = new TreeMap<>();
    private final Object partialSheetsLock = new Object();

    @Getter
    private final ExCollection collection;
    @Getter
    private final Header header;
    @Getter
    private final Language language;

    public DataSheet(ExCollection collection, Header header, Language language, Class<T> dataRowClass) {
        this.collection = collection;
        this.header = header;
        this.language = language;
        this.dataRowClass = dataRowClass;
        log.debug("instanced: {}, {}", header.getName(), language);
    }

    protected ISheet<T> createPartialSheet(Page page, PackFile file) {
        return new PartialDataSheet<>(this, page, file, dataRowClass);
    }

    protected PackFile getPartialFile(Page page) {
        String partialFileName = String.format("exd/%s_%d%s.exd", header.getName().toLowerCase(), page.getStart(), language.getSuffix());
        return collection.getPackCollection().tryGetFile(partialFileName);
    }

    protected ISheet<T> getPartialSheet(int row) {
        if (rowToPartialSheetMap.containsKey(row))
            return rowToPartialSheetMap.get(row);

        List<Page> pages = Arrays.stream(header.getPages()).filter(it -> it.contains(row)).collect(Collectors.toList());
        if (pages.size() == 0) {
            log.warn("Page not found, row:{}, sheet:{}", row, header.getName());
            return null;
            //throw new IndexOutOfBoundsException("Index out of range: " + row);
        }
        synchronized (partialSheetsLock) {
            Page page = pages.get(0);

            ISheet<T> partial = partialSheets.get(page);
            if (partial == null) {
                partial = createPartialSheet(page);
            }
            return partial;
        }
    }

    private void createAllPartialSheets() {
        synchronized (partialSheetsLock) {
            if (partialSheetsCreated) {
                return;
            }

            for (Page page : header.getPages()) {
                if (partialSheets.containsKey(page)) {
                    continue;
                }
                createPartialSheet(page);
            }

            partialSheetsCreated = true;
        }
    }

    private ISheet<T> createPartialSheet(Page page) {
        PackFile file = getPartialFile(page);

        ISheet<T> partial = createPartialSheet(page, file);
        partialSheets.put(page, partial);
        for (Integer key :  partial.getKeys()) {
            rowToPartialSheetMap.put(key, partial);
        }
        return partial;
    }

    @Override
    public String getName() {
        return header.getName() + language.getSuffix();
    }

    @Override
    public int getCount() {
        createAllPartialSheets();
        return partialSheets.values().stream().mapToInt(ISheet::getCount).sum();
    }

    @Override
    public Collection<Integer> getKeys() {
        createAllPartialSheets();
        return rowToPartialSheetMap.keySet();
    }

    @Override
    public boolean containsRow(int row) {
        createAllPartialSheets();
        return rowToPartialSheetMap.containsKey(row);
    }

    @Override
    public ByteBuffer getBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int row) {
        ISheet<T> sheet = getPartialSheet(row);
        return sheet == null ? null : sheet.get(row);
    }

    @Override
    public Object get(int row, int column) {
        T t = get(row);
        return t == null ? null : t.get(column);
    }

    @Override
    public Iterator<T> iterator() {
        createAllPartialSheets();
        Iterator<Integer> rowItor = rowToPartialSheetMap.keySet().iterator();
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return rowItor.hasNext();
            }

            @Override
            public T next() {
                Integer key = rowItor.next();
                return rowToPartialSheetMap.get(key).get(key);
            }
        };
    }
}
