package ffxiv.housim.saintcoinach.ex.sheet;

import ffxiv.housim.saintcoinach.ex.ExCollection;
import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.row.IDataRow;
import ffxiv.housim.saintcoinach.ex.row.IMultiRow;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiSheet<TMulti extends IMultiRow, TData extends IDataRow>
        implements IMultiSheet<TMulti, TData> {

    private Class<TMulti> clazz;

    private final Map<Language, ISheet<TData>> localisedSheets = new ConcurrentHashMap<>();
    private final Map<Integer, TMulti> rows = new ConcurrentHashMap<>();

    @Getter
    private final ExCollection collection;
    @Getter
    private final Header header;

    public MultiSheet(ExCollection collection, Header header) {
        this.collection = collection;
        this.header = header;

        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types != null && types.length > 0) {
                clazz = (Class<TMulti>) types[0];
            }
        }
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
            throw new IllegalArgumentException("Unsupported language " + language);
        }

        sheet = createLocalisedSheet(language);

        localisedSheets.put(language, sheet);
        return sheet;
    }

    protected TMulti createMultiRow(int row) {
        TMulti result = null;
        try {
            Constructor<TMulti> constructor = clazz.getConstructor(IMultiSheet.class, int.class);
            result = constructor.newInstance(this, row);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected ISheet<TData> createLocalisedSheet(Language language) {
        return new DataSheet<>(collection, header, language);
    }
}
