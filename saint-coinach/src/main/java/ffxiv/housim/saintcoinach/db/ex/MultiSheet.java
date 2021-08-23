package ffxiv.housim.saintcoinach.db.ex;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MultiSheet<TMulti extends IMultiRow, TData extends IDataRow> implements IMultiSheet<TMulti, TData> {

    protected Constructor<TMulti> multiRowConstructor;

    protected final Class<TData> dataRowClass;

    private final Map<Language, ISheet<TData>> localisedSheets = new ConcurrentHashMap<>();
    private final Map<Integer, TMulti> rows = new ConcurrentHashMap<>();

    @Getter
    private final ExCollection collection;
    @Getter
    private final Header header;

    public MultiSheet(ExCollection collection, Header header, Class<TMulti> multiRowClass, Class<TData> dataRowClass) {
        this.collection = collection;
        this.header = header;

        this.dataRowClass = dataRowClass;

        try {
            multiRowConstructor = multiRowClass.getConstructor(IMultiSheet.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        log.debug("instanced: {}", header.getName());
    }

    @Override
    public ISheet<TData> getActiveSheet() {
        return getLocalisedSheet(collection.getActiveLanguage());
    }

    @Override
    public String getName() {
        return header.getName();
    }

    @Override
    public int getCount() {
        return getActiveSheet().getCount();
    }

    @Override
    public Collection<Integer> getKeys() {
        return getActiveSheet().getKeys();
    }

    @Override
    public boolean containsRow(int row) {
        return getActiveSheet().containsRow(row);
    }

    @Override
    public TMulti get(int key) {
        TMulti row = rows.get(key);
        if (row != null) {
            return row;
        }

        row = createMultiRow(key);
        rows.put(key, row);

        return row;
    }

    @Override
    public Object get(int row, int column) {
        return get(row).get(column);
    }

    @Override
    public ISheet<TData> getLocalisedSheet(Language language) {
        ISheet<TData> sheet = localisedSheets.get(language);
        if (sheet != null) {
            return sheet;
        }

        boolean containsLanguage = false;
        Language[] languages = header.getAvailableLanguages();
        for (Language lang : languages) {
            if (lang == language) {
                containsLanguage = true;
                break;
            }
        }

        if (!containsLanguage) {
            throw new IllegalArgumentException("Unsupported language " + language + ", languages: " + Arrays.toString(languages));
        }

        sheet = createLocalisedSheet(language);

        localisedSheets.put(language, sheet);
        return sheet;
    }

    protected TMulti createMultiRow(int row) {
        try {
            return multiRowConstructor.newInstance(this, row);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected ISheet<TData> createLocalisedSheet(Language language) {
        return new DataSheet<>(collection, header, language, dataRowClass);
    }

    @Override
    public Iterator<TMulti> iterator() {
        ISheet<TData> sheet = getActiveSheet();

        Iterator<Integer> it = sheet.getKeys().iterator();
        return new Iterator<TMulti>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public TMulti next() {
                return get(it.next());
            }
        };
    }
}
