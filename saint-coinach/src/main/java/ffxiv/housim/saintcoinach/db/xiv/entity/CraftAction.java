package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class CraftAction extends ClassJobActionBase {
    public CraftAction(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getDescription() {
        return asString("Description");
    }

    public int getCost() {
        return asInt32("Cost");
    }
}
