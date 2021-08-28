package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;

public abstract class ItemBase extends XivRow implements IQuantifiable {

    public ItemBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public String getSingular() {
        return asString("Singular");
    }

    public String getPlural() {
        return asString("Plural");
    }

    public String getDescription() {
        return asString("Description");
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public int getStackSize() {
        return asInt32("StackSize");
    }

    @Override
    public String toString() {
        return getName();
    }
}
