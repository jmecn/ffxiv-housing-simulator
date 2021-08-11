package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.ISheet;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.MultiSheet;
import lombok.Getter;

public class RelationalMultiSheet<TMulti extends IRelationalMultiRow, TData extends IRelationalDataRow> extends MultiSheet<TMulti, TData> implements IRelationalMultiSheet<TMulti, TData> {

    @Getter
    private RelationalExCollection collection;
    @Getter
    private RelationalHeader header;

    public RelationalMultiSheet(RelationalExCollection collection, RelationalHeader header, Class<TMulti> multiRowClass, Class<TData> dataRowClass) {
        super(collection, header, multiRowClass, dataRowClass);
        this.collection = collection;
        this.header = header;
    }

    @Override
    public IRelationalSheet<TData> getActiveSheet() {
        return (IRelationalSheet<TData>) super.getActiveSheet();
    }

    @Override
    public IRelationalSheet<TData> getLocalisedSheet(Language language) {
        return (IRelationalSheet<TData>) super.getLocalisedSheet(language);
    }

    @Override
    protected ISheet<TData> createLocalisedSheet(Language language) {
        return new RelationalDataSheet<>(collection, header, language, dataRowClass);
    }

    @Override
    public Object get(int row, String columnName) {
        return get(row).get(columnName);
    }

    @Override
    public IRelationalRow indexedLookup(String index, int key) {
        return getActiveSheet().indexedLookup(index, key);
    }
}
