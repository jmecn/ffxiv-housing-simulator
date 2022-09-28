package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class BNpcBase extends XivRow {
    public BNpcBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public double getScale() {
        return asDouble("Scale");
    }

    public ModelChara getModelChara() {
        return as(ModelChara.class);
    }

    public BNpcCustomize getBNpcCustomize() {
        return as(BNpcCustomize.class);
    }

    public NpcEquip getNpcEquip() {
        return as(NpcEquip.class);
    }
}
