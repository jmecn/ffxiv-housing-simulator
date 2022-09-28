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
public abstract class ActionBase extends XivRow {
    ActionTransient actionTransient;
    public ActionBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public String getDescription() {
        return getActionTransient().getDescription();
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public ActionTransient getActionTransient() {
        if (actionTransient == null) {
            getSheet().getCollection().getSheet(ActionTransient.class).get(getKey());
        }
        return actionTransient;
    }

    public String toString() {
        return getName();
    }
}
