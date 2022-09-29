package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

import java.util.Collection;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class InstanceContent extends ContentBase implements IItemSource {

    private InstanceContentData data;

    protected InstanceContent(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    @Override
    public Collection<IContentReward> getFixedRewards() {
        return null;
    }

    @Override
    public Collection<Item> getItems() {
        return null;
    }
    // TODO need implementation
}
