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
public class ParamGrow extends XivRow {
    public ParamGrow(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getExpToNext() {
        return asInt32("ExpToNext");
    }

    public int getAdditionalActions() {
        return asInt32("AdditionalActions");
    }

    public double getMpModifier() {
        return asInt32("MpModifier") / 100.0;
    }
}
