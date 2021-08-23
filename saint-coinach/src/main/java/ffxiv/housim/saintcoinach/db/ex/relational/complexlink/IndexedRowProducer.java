package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.db.ex.IRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;

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