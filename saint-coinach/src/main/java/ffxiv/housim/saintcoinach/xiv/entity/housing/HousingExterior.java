package ffxiv.housim.saintcoinach.xiv.entity.housing;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;
import ffxiv.housim.saintcoinach.xiv.entity.PlaceName;

public class HousingExterior extends XivRow {
    public HousingExterior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getExteriorKey() {
        return asInt16("ExteriorKey");
    }

    public int getExteriorType() {
        return asInt16("ExteriorType");
    }

    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    public short getHousingSize() {
        return asInt16("HousingSize");
    }

    public String getModel() {
        return asString("Model");
    }
}
