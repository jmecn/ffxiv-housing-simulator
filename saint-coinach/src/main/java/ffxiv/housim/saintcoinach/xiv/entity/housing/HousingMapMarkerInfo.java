package ffxiv.housim.saintcoinach.xiv.entity.housing;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;
import ffxiv.housim.saintcoinach.xiv.XivSubRow;

public class HousingMapMarkerInfo extends XivSubRow {
    public HousingMapMarkerInfo(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
