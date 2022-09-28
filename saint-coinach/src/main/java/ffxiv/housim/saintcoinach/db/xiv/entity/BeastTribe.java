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
public class BeastTribe extends XivRow {
    public BeastTribe(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public String getRelationName() {
        return asString("Name{Relation}");
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public ImageFile getReputationIcon() {
        return asImage("Icon{Reputation}");
    }

    @Override
    public String toString() {
        return getName();
    }
}
