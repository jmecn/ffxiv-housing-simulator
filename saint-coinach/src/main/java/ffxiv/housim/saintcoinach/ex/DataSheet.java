package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.Range;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class DataSheet<T extends IDataRow> implements IDataSheet<T> {

    private boolean partialSheetsCreated = false;
    private final Class<T> clazz;

    private final Map<Range, ISheet<T>> partialSheets = new HashMap<>();
    private final Map<Integer, ISheet<T>> rowToPartialSheetMap = new TreeMap<>();
    private final Object partialSheetsLock = new Object();

    @Getter
    private final ExCollection collection;
    @Getter
    private final Header header;
    @Getter
    private final Language language;

    public DataSheet(ExCollection collection, Header header, Language language, Class<T> clazz) {
        this.collection = collection;
        this.header = header;
        this.language = language;
        this.clazz = clazz;
    }

    protected ISheet<T> createPartialSheet(Range range, PackFile file) {
        return new PartialDataSheet<>(this, range, file, clazz);
    }

    protected PackFile getPartialFile(Range range) {
        String partialFileName = String.format("exd/%s_%d%s.exd", header.getName().toLowerCase(), range.getStart(), language.getSuffix());
        return collection.getPackCollection().tryGetFile(partialFileName);
    }

    protected ISheet<T> getPartialSheet(int row) {
        if (rowToPartialSheetMap.containsKey(row))
            return rowToPartialSheetMap.get(row);

        List<Range> ranges = Arrays.stream(header.getDataFileRanges()).filter(it -> it.contains(row)).collect(Collectors.toList());
        if (ranges.size() == 0) {
            throw new IndexOutOfBoundsException("Index out of range: " + row);
        }
        synchronized (partialSheetsLock) {
            Range range = ranges.get(0);

            ISheet<T> partial = partialSheets.get(range);
            if (partial == null) {
                partial = createPartialSheet(range);
            }
            return partial;
        }
    }

    private void createAllPartialSheets() {
        synchronized (partialSheetsLock) {
            if (partialSheetsCreated) {
                return;
            }

            for (Range range : header.getDataFileRanges()) {
                if (partialSheets.containsKey(range)) {
                    continue;
                }
                createPartialSheet(range);
            }

            partialSheetsCreated = true;
        }
    }

    private ISheet<T> createPartialSheet(Range range) {
        PackFile file = getPartialFile(range);

        ISheet<T> partial = createPartialSheet(range, file);
        partialSheets.put(range, partial);
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
        // TODO maybe not need stream
        return partialSheets.values().stream().mapToInt(ISheet::getCount).sum();
    }

    @Override
    public Collection<Integer> getKeys() {
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
        return getPartialSheet(row).get(row);
    }

    @Override
    public Object get(int row, int column) {
        return get(row).get(column);
    }

}
