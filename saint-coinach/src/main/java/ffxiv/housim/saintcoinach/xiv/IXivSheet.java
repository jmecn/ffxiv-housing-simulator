package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalSheet;

public interface IXivSheet<T extends IXivRow> extends IRelationalSheet<T> {

    XivCollection getCollection();

}
