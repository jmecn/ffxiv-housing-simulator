package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;

public class IndexedRowProducer implements IRowProducer {
    public String keyColumnName;

    public IndexedRowProducer(String keyColumnName) {
        this.keyColumnName = keyColumnName;
    }

    @Override
    public IRow getRow(IRelationalSheet sheet, int key) {
        return sheet.indexedLookup(keyColumnName, key);
    }
}