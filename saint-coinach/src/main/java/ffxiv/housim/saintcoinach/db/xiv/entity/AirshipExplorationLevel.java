package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class AirshipExplorationLevel extends XivRow {
    public AirshipExplorationLevel(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
    
    public int getExpToNext() {
        return asInt32("ExpToNext");
    }
}
