package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

public class HousingFurniture extends HousingItem {
    public final static String FORMAT = "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb";
    public HousingFurniture(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getSgbPath() {
        int modelKey = getModelKey();
        return String.format(FORMAT, modelKey, modelKey);
    }
}
