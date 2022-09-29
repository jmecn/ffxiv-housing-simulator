package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;

import java.util.Collection;
import java.util.Collections;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class Achievement extends XivRow implements IItemSource {
    public Achievement(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public AchievementCategory getAchievementCategory() {
        return as(AchievementCategory.class);
    }

    public String getName() {
        return asString("Name");
    }

    public String getDescription() {
        return asString("Description");
    }

    public int getPoints() {
        return asInt32("Points");
    }

    public Title getTitle() {
        return as(Title.class);
    }

    public Item getItem() {
        return as(Item.class, "Item");
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public int getOrder() {
        return asInt32("Order");
    }

    @Override
    public Collection<Item> getItems() {
        return Collections.singletonList(getItem());
    }

    @Override
    public String toString() {
        return getName();
    }

}
