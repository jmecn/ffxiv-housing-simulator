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
public class BeastReputationRank extends XivRow {
    public BeastReputationRank(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public int getRequiredReputation() {
        return asInt32("RequiredReputation");
    }

    @Override
    public String toString() {
        return getName();
    }
}
