package ffxiv.housim.saintcoinach.db.ex;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PartialDataSheet<T extends IDataRow> implements IDataSheet<T> {
    final static int HeaderLengthOffset = 0x08;

    final static int EntriesOffset = 0x20;
    final static int EntryLength = 0x08;

    private final Class<T> dataRowClass;
    private final Map<Integer, T> rows = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> rowOffsets = new TreeMap<>();

    @Getter
    private final IDataSheet<T> sourceSheet;
    @Getter
    private final Page page;
    @Getter
    private final PackFile file;

    public PartialDataSheet(IDataSheet<T> sourceSheet, Page page, PackFile file, Class<T> dataRowClass) {
        this.sourceSheet = sourceSheet;
        this.page = page;
        this.file = file;

        this.dataRowClass = dataRowClass;

        build();

        log.debug("instanced: {}, {}", sourceSheet.getHeader().getName(), page);
    }

    @Override
    public Language getLanguage() {
        return sourceSheet.getLanguage();
    }

    @Override
    public int getCount() {
        return rowOffsets.size();
    }

    @Override
    public ByteBuffer getBuffer() {
        ByteBuffer buffer = ByteBuffer.wrap(file.getData());
        buffer.order(ByteOrder.BIG_ENDIAN);

        return buffer;
    }

    @Override
    public Collection<Integer> getKeys() {
        return rowOffsets.keySet();
    }

    @Override
    public String getName() {
        return sourceSheet.getName() + "_" + page.getStart();
    }

    @Override
    public Header getHeader() {
        return sourceSheet.getHeader();
    }

    @Override
    public ExCollection getCollection() {
        return sourceSheet.getCollection();
    }

    @Override
    public boolean containsRow(int row) {
        return rowOffsets.containsKey(row);
    }

    public Iterable<T> getAllRows() {
        return rows.values();
    }

    protected T createRow(int row, int offset) {
        T result = null;
        try {
            Constructor<T> constructor = dataRowClass.getConstructor(IDataSheet.class, int.class, int.class);
            result = constructor.newInstance(this, row, offset);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public T get(int row) {
        T result = rows.get(row);
        if (result != null) {
            return result;
        }

        Integer offset = rowOffsets.get(row);
        if (offset == null) {
            throw new IllegalArgumentException("Unknown row " + row);
        }

        result = createRow(row, offset);

        if (result != null) {
            rows.put(row, result);
        }

        return result;
    }

    @Override
    public Object get(int row, int column) {
        return get(row).get(column);
    }

    private void build() {
        byte[] data = file.getData();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.position(HeaderLengthOffset);
        int headerLen = buffer.getInt();
        int count = headerLen / EntryLength;

        buffer.position(EntriesOffset);
        for (int i = 0; i < count; i++) {
            int key = buffer.getInt();
            int offset = buffer.getInt();
            rowOffsets.put(key, offset);
        }
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<Integer> it = rowOffsets.keySet().iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
            @Override
            public T next() {
                return get(it.next());
            }
        };
    }
}
