package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class HousingLandSet extends XivRow {
    public HousingLandSet(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public Object getPlotSize(int index) {
        return get("PlotSize[" + index + "]");
    }

    public Object getMinPrice(int index) {
        return get("MinPrice[" + index + "]");
    }

    public Object getInitialPrice(int index) {
        return get("InitialPrice[" + index + "]");
    }
}