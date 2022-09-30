package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/30
 */
public class Behavior extends XivSubRow {
    public Behavior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

}
