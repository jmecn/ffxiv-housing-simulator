package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class ENpcResident extends XivRow implements IQuantifiableXivString {
    public ENpcResident(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
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

    public String getTitle() {
        return asString("Title");
    }

    @Override
    public String toString() {
        return getSingular();
    }
}
