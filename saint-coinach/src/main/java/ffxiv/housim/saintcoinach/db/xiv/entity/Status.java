package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class Status extends XivRow {
    public Status(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public String getDescription() {
        return asString("Description");
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public boolean canDispel() {
        return asBoolean("CanDispel");
    }

    public short getCategory() {
        return asInt16("Category");
    }

    @Override
    public String toString() {
        return getName();
    }
}
