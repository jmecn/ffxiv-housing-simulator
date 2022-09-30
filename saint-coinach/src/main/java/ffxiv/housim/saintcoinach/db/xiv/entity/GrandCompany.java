package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class GrandCompany extends XivRow {

    private static final int SEAL_ITEM_OFFSET = 19;

    public GrandCompany(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public Item getSealItem() {
        return getSheet().getCollection().getSheet(Item.class).get(getKey() + SEAL_ITEM_OFFSET);
    }

    @Override
    public String toString() {
        return getName();
    }
}
