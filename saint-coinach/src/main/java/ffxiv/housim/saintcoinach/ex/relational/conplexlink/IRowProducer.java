package ffxiv.housim.saintcoinach.ex.relational.conplexlink;

import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;

public interface IRowProducer {
    IRow getRow(IRelationalSheet sheet, int key);
}