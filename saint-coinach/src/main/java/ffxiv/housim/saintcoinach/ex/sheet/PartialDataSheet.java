package ffxiv.housim.saintcoinach.ex.sheet;

import ffxiv.housim.saintcoinach.Range;
import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.row.IDataRow;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PartialDataSheet<T extends IDataRow> implements IDataSheet<T> {
    final static int HeaderLengthOffset = 0x08;

    final static int EntriesOffset = 0x20;
    final static int EntryLength = 0x08;

    private Class<T> clazz;
    private final Map<Integer, T> rows = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> rowOffsets = new TreeMap<>();

    @Getter
    private final IDataSheet<T> sourceSheet;
    @Getter
    private final Range range;
    @Getter
    private final PackFile file;

    public PartialDataSheet(IDataSheet<T> sourceSheet, Range range, PackFile file, Class<T> clazz) {
        this.sourceSheet = sourceSheet;
        this.range = range;
        this.file = file;

        this.clazz = clazz;

        build();
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
        return sourceSheet.getName() + "_" + range.getStart();
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

        try {
            Constructor<T> constructor = clazz.getConstructor(IDataSheet.class, int.class, int.class);
            result = constructor.newInstance(this, row, offset);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

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
}
