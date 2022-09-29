package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.util.Collection;
import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class FccShop extends XivRow implements IShop {

    private List<ENpc> eNpcs;
    private List<IShopListing> shopListings;
    private List<Item> items;

    public FccShop(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    @Override
    public Collection<? extends Item> getItems() {
        return null;
    }

    @Override
    public String getName() {
        return asString("Name");
    }

    @Override
    public Iterable<ENpcs> getENpcs() {
        return null;
    }

    @Override
    public Iterable<IShopListing> getShopListings() {
        return null;
    }

    public String toString() {
        return getName();
    }
    // TODO need implementation
}
