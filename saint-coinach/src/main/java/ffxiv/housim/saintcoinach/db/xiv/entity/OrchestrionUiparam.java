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
public class OrchestrionUiparam extends XivRow {
    public OrchestrionUiparam(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public OrchestrionCategory getOrchestrionCategory() {
        return as(OrchestrionCategory.class);
    }

    public short getOrder() {
        return asInt16("Order");
    }

    public String toString() {
        return String.format("%s#%d", getOrchestrionCategory(), getOrder());
    }
}
