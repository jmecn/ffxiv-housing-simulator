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
public class Title extends XivRow {
    public Title(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getMasculine() {
        return asString("Masculine");
    }

    public String getFeminine() {
        return asString("Feminine");
    }

    public boolean isPrefix() {
        return asBoolean("IsPrefix");
    }

    public String toString() {
        return String.format("%s / %s", getFeminine(), getMasculine());
    }
}
