package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.*;

public class RelationalDataSheet<T extends IRelationalDataRow> extends DataSheet<T> implements IRelationalDataSheet<T> {

    public RelationalDataSheet(RelationalExCollection collection, RelationalHeader header, Language language, Class<T> clazz) {
        super(collection, header, language, clazz);
    }

    @Override
    public RelationalExCollection getCollection() {
        return (RelationalExCollection) super.getCollection();
    }

    @Override
    public RelationalHeader getHeader() {
        return (RelationalHeader) super.getHeader();
    }

    @Override
    public Object get(int row, String columnName) {
        return null;
    }

    @Override
    public IRelationalRow indexedLookup(String index, int key) {
        return null;
    }
}
