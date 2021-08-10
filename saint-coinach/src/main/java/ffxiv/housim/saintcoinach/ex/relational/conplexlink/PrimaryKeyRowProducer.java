package ffxiv.housim.saintcoinach.ex.relational.conplexlink;

import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;

public class PrimaryKeyRowProducer implements IRowProducer {
    @Override
    public IRow getRow(IRelationalSheet sheet, int key) {
        return sheet.get(key);
    }
}