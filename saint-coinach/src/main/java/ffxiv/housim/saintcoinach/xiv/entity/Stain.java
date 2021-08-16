package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public class Stain extends XivRow {
    public Stain(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getColor() {
        return asInt32("Color");
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
