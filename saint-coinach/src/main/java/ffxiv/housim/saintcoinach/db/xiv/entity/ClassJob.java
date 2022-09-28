package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import ffxiv.housim.saintcoinach.utils.IconHelper;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class ClassJob extends XivRow {

    private static final int ICON_OFFSET = 62000;
    private static final int FRAMED_ICON_OFFSET = 62100;

    public ClassJob(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public String getAbbreviation() {
        return asString("Abbreviation");
    }

    public ClassJobCategory getClassJobCategory() {
        return as(ClassJobCategory.class);
    }

    public ClassJob getParentClassJob() {
        return as(ClassJob.class, "ClassJob{Parent}");
    }

    public Item getStartingWeapon() {
        return as(Item.class, "Item{StartingWeapon}");
    }

    public Item getSoulCrystal() {
        return as(Item.class, "Item{SoulCrystal}");
    }

    public short getStartingLevel() {
        return asInt16("StartingLevel");
    }

    public ImageFile getIcon() {
        int nr = ICON_OFFSET + getKey();
        return IconHelper.getIcon(getSheet().getCollection().getPackCollection(), nr);
    }

    public ImageFile getFramedIcon() {
        int nr = FRAMED_ICON_OFFSET + getKey();
        return IconHelper.getIcon(getSheet().getCollection().getPackCollection(), nr);
    }

    @Override
    public String toString() {
        return getName();
    }
}
