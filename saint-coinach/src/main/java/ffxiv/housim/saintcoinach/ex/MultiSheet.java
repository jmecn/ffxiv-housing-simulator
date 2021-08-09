package ffxiv.housim.saintcoinach.ex;

import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiSheet<TData extends IDataRow> implements IMultiSheet<MultiRow, TData> {

    private Class<TData> dataRowClazz;

    private final Map<Language, ISheet<TData>> localisedSheets = new ConcurrentHashMap<>();
    private final Map<Integer, MultiRow> rows = new ConcurrentHashMap<>();

    @Getter
    private final ExCollection collection;
    @Getter
    private final Header header;

    public MultiSheet(ExCollection collection, Header header, Class<TData> dataRowClass) {
        this.collection = collection;
        this.header = header;

        this.dataRowClazz = dataRowClass;
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
    public MultiRow get(int key) {
        MultiRow row = rows.get(key);
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

    protected MultiRow createMultiRow(int row) {
        return new MultiRow(this, row);
    }

    protected ISheet<TData> createLocalisedSheet(Language language) {
        return new DataSheet<>(collection, header, language, dataRowClazz);
    }
}
