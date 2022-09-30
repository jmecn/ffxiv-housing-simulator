package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.ENpcBase;

import java.util.Arrays;

public class HousingEmploymentNpcList extends XivSubRow {
    public HousingEmploymentNpcList(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
    public HousingEmploymentNpcRace getRace() {
        return as(HousingEmploymentNpcRace.class, "Race");
    }
    public int[] getENpcBase() {
        return asIntArray("ENpcBase");
    }

    @Override
    public String toString() {
        return "HousingEmploymentNpcList{" +
                "race=" + getRace() +
                ", ENpcBase=" + Arrays.toString(getENpcBase()) +
                '}';
    }
}
