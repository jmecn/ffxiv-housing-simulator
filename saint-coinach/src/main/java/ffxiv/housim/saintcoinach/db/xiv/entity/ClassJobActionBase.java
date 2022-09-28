package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public abstract class ClassJobActionBase extends ActionBase {
    public ClassJobActionBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public ClassJob getClassJob() {
        return as(ClassJob.class);
    }

    public ClassJobCategory getClassJobCategory() {
        return as(ClassJobCategory.class);
    }

    public int getClassJobLevel() {
        return asInt32("ClassJobLevel");
    }
}
