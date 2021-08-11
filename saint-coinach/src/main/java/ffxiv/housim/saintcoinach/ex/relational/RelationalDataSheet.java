package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.Page;
import ffxiv.housim.saintcoinach.ex.*;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class RelationalDataSheet<T extends IRelationalDataRow> extends DataSheet<T> implements IRelationalDataSheet<T> {

    private Map<String, RelationalDataIndex<T>> indexes = new ConcurrentHashMap<>();

    @Getter
    private RelationalExCollection collection;
    @Getter
    private RelationalHeader header;

    public RelationalDataSheet(RelationalExCollection collection, RelationalHeader header, Language language, Class<T> clazz) {
        super(collection, header, language, clazz);
        this.collection = collection;
        this.header = header;
    }

    @Override
    protected ISheet<T> createPartialSheet(Page page, PackFile file) {
        return new RelationalPartialDataSheet<T>(this, page, file, dataRowClass);
    }

    @Override
    public Object get(int row, String columnName) {
        return get(row).get(columnName);
    }

    @Override
    public IRelationalRow indexedLookup(String indexName, int key) {
        if (key == 0) {
            return null;
        }

        RelationalDataIndex<T> index = indexes.get(indexName);
        if (index == null) {
            RelationalColumn col = header.findColumn(indexName);
            if (col == null) {
                throw new NoSuchElementException("index not found " + indexName);
            }

            index = new RelationalDataIndex<>(this, col);
            indexes.put(indexName, index);
        }

        return index.get(key);
    }
}
