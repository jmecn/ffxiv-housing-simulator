package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.Page;
import ffxiv.housim.saintcoinach.ex.PartialDataSheet;
import ffxiv.housim.saintcoinach.io.PackFile;

public class RelationalPartialDataSheet<T extends IRelationalDataRow>
        extends PartialDataSheet<T> implements IRelationalDataSheet<T> {

    public RelationalPartialDataSheet(IRelationalDataSheet<T> sourceSheet, Page page, PackFile file, Class<T> clazz) {
        super(sourceSheet, page, file, clazz);
    }

    @Override
    public IRelationalDataSheet<T> getSourceSheet() {
        return (IRelationalDataSheet<T>) super.getSourceSheet();
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
        return get(row).get(columnName);
    }

    @Override
    public IRelationalRow indexedLookup(String index, int key) {
        throw new UnsupportedOperationException("Indexes are not supported in partial sheets.");
    }

}