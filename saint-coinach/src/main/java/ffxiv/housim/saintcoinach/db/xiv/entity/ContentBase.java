package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.util.Collection;

/**
 * Base class representing duties.
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public abstract class ContentBase extends XivRow {

    protected ContentBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    /**
     * Gets the name of the current content.
     * @return
     */
    public String getName() {
        return asString("Name");
    }

    public String getDescription() {
        return asString("Description");
    }

    public boolean isInDutyFinder() {
        return asBoolean("IsInDutyFinder");
    }

    public abstract Collection<IContentReward> getFixedRewards();
}
