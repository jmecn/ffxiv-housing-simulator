package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.ex.relational.RelationalHeader;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XivSheet<T extends IXivRow> implements IXivSheet<T> {

    protected Map<Integer, T> rows = new ConcurrentHashMap<>();
    protected IRelationalSheet<T> source;

    private XivCollection collection;

    private Class<T> rowClass;
    private Constructor<T> rowConstructor;

    public XivSheet(XivCollection collection, IRelationalSheet<T> source, Class<T> rowClass) {
        this.collection = collection;
        this.source = source;
        this.rowClass = rowClass;
    }

    public Constructor<T> getConstructor() throws NoSuchMethodException {
        if (rowConstructor == null) {
            rowConstructor = rowClass.getConstructor(getClass(), IRelationalRow.class);
        }
        return rowConstructor;
    }

    protected T createRow(IRelationalRow sourceRow) throws ReflectiveOperationException{
        return getConstructor().newInstance(this, sourceRow);
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
            throw new IllegalArgumentException("No such row:" + row);
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
                T sourceRow = itor.next();
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
