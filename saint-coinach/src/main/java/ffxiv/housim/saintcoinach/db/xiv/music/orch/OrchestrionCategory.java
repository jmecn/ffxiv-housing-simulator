package ffxiv.housim.saintcoinach.db.xiv.music.orch;

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
public class OrchestrionCategory extends XivRow {
    public OrchestrionCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public short getHideOrder() {
        return asInt16("HideOrder");
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public short getOrder() {
        return asInt16("Order");
    }

    @Override
    public String toString() {
        return getName();
    }
}
