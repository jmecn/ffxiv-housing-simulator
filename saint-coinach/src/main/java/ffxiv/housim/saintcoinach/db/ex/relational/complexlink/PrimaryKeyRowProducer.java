package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.db.ex.IRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;

public class PrimaryKeyRowProducer implements IRowProducer {
    @Override
    public IRow getRow(IRelationalSheet sheet, int key) {
        return sheet.get(key);
    }
}