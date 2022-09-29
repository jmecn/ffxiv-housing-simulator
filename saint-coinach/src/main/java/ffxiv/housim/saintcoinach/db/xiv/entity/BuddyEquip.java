package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class BuddyEquip extends XivRow implements IQuantifiableXivString {
    public BuddyEquip(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
    
    public String getName() {
        return asString("Name");
    }
    @Override
    public String getSingular() {
        return asString("Singular");
    }
    @Override
    public String getPlural() {
        if (getSheet().getCollection().getActiveLanguage() == Language.Japanese) {
            return getSingular();
        } else {
            return asString("Plural");
        }
    }
    public GrandCompany getGrandCompany() {
        return as(GrandCompany.class);
    }
    public ImageFile getHeadIcon() {
        return asImage("Icon{Head}");
    }
    public ImageFile getBodyIcon() {
        return asImage("Icon{Body}");
    }
    public ImageFile getLegsIcon() {
        return asImage("Icon{Legs}");
    }
    @Override
    public String toString() {
        return getName();
    }
}
