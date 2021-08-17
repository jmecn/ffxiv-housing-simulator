package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.ex.relational.RelationalHeader;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class XivSheet<T extends IXivRow> implements IXivSheet<T> {

    protected Map<Integer, T> rows = new ConcurrentHashMap<>();
    protected IRelationalSheet<T> source;

    private final XivCollection collection;

    private Constructor<T> rowConstructor;

    public XivSheet(XivCollection collection, IRelationalSheet<T> source, Class<T> rowClass) {
        this.collection = collection;
        this.source = source;
        try {
            this.rowConstructor = rowClass.getConstructor(IXivSheet.class, IRelationalRow.class);
        } catch (NoSuchMethodException e) {
            // This should not happen.
            e.printStackTrace();
        }
    }

    protected T createRow(IRelationalRow sourceRow) throws ReflectiveOperationException{
        return rowConstructor.newInstance(this, sourceRow);
    }

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public int getCount() {
        return source.getCount();
    }

    @Override
    public RelationalHeader getHeader() {
        return source.getHeader();
    }

    @Override
    public Collection<Integer> getKeys() {
        return source.getKeys();
    }

    @Override
    public boolean containsRow(int row) {
        return source.containsRow(row);
    }

    @Override
    public T get(int row) {
        T t = rows.get(row);
        if (t != null) {
            return t;
        }

        if (!source.containsRow(row)) {
            log.warn("No such row: {}#{}", source.getName(), row);
            return null;
        }

        try {
            t = createRow(source.get(row));
            rows.put(row, t);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public Object get(int row, String columnName) {
        return get(row).get(columnName);
    }

    @Override
    public Object get(int row, int column) {
        return get(row).get(column);
    }

    @Override
    public IRelationalRow indexedLookup(String index, int key) {
        return source.indexedLookup(index, key);
    }

    @Override
    public XivCollection getCollection() {
        return collection;
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> itor = source.iterator();

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return itor.hasNext();
            }

            @Override
            public T next() {
                IRelationalRow sourceRow = itor.next();
                int key = sourceRow.getKey();
                T t = rows.get(key);
                if (t == null) {
                    try {
                        t = createRow(sourceRow);
                        rows.put(key, t);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
                return t;
            }
        };
    }
}
