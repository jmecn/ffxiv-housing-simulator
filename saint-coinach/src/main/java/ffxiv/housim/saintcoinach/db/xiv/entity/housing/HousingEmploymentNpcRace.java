package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class HousingEmploymentNpcRace extends XivRow {
    public HousingEmploymentNpcRace(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
    public String getRace() {
        return asString("Race");
    }

    @Override
    public String toString() {
        return getRace();
    }
}
