package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.math.XivColor;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class Stain extends XivRow {
    public Stain(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public XivColor getColor() {
        return as(XivColor.class);
    }

    public short getShade() {
        return asInt16("Shade");
    }

    public short getSubOrder() {
        return asInt16("SubOrder");
    }

    public String getName() {
        return asString("Name");
    }

    public boolean get4() {
        return (boolean) get(4);
    }

    public boolean get5() {
        return (boolean) get(5);
    }
}
