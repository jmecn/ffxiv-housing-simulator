package ffxiv.housim.saintcoinach.db.xiv.entity;

import com.google.common.collect.Lists;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class Race extends XivRow {
    private List<Item> maleRse;
    private List<Item> femaleRse;

    public Race(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getMasculine() {
        return asString("Masculine");
    }

    public String getFeminine() {
        return asString("Feminine");
    }

    public List<Item> getMaleRse() {
        if (maleRse == null) {
            maleRse = Lists.newArrayList(
                    as(Item.class, "RSE{M}{Body}"),
                    as(Item.class, "RSE{M}{Hands}"),
                    as(Item.class, "RSE{M}{Legs}"),
                    as(Item.class, "RSE{M}{Feet}")
            );
        }
        return maleRse;
    }

    public List<Item> getFemaleRse() {
        if (femaleRse == null) {
            femaleRse = Lists.newArrayList(
                    as(Item.class, "RSE{F}{Body}"),
                    as(Item.class, "RSE{F}{Hands}"),
                    as(Item.class, "RSE{F}{Legs}"),
                    as(Item.class, "RSE{F}{Feet}")
            );
        }
        return femaleRse;
    }
}