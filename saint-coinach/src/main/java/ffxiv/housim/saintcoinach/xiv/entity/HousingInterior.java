package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public class HousingInterior extends XivRow {
    public HousingInterior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
