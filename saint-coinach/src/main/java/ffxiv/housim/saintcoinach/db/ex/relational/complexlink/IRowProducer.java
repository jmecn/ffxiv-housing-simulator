package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.db.ex.IRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;

public interface IRowProducer {
    IRow getRow(IRelationalSheet sheet, int key);
}