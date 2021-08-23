package ffxiv.housim.saintcoinach.db.xiv;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;

public interface IXivSheet<T extends IXivRow> extends IRelationalSheet<T> {

    XivCollection getCollection();

}
