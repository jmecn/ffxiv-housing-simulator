package ffxiv.housim.saintcoinach.db.xiv.entity;

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
public class BuddyAction extends XivRow {
    public BuddyAction(IXivSheet sheet, IRelationalRow sourceRow) {
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
    
    public ImageFile getStatusIcon() {
        return asImage("Icon{Status}");
    }

    @Override
    public String toString() {
        return getName();
    }
}
