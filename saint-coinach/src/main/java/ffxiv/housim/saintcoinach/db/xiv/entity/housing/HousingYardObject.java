package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

public class HousingYardObject extends HousingItem {
    public final static String SgbPathFormat = "bgcommon/hou/outdoor/general/%04d/asset/gar_b0_m%04d.sgb";

    public HousingYardObject(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getSgbPath() {
        int modelKey = getModelKey();
        return String.format(SgbPathFormat, modelKey, modelKey);
    }

    // TODO GetScene
}
