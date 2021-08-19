package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.imaging.ImageFile;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public class Weather extends XivRow {
    public Weather(IXivSheet sheet, IRelationalRow sourceRow) {
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

    @Override
    public String toString() {
        return getName();
    }
}
