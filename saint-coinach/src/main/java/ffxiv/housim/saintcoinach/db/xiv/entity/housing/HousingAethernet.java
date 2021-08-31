package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.Level;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.PlaceName;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;

// 房区
public class HousingAethernet extends XivRow {
    public HousingAethernet(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public Level getLevel() {
        return as(Level.class);
    }

    public TerritoryType getTerritoryType() {
        return as(TerritoryType.class);
    }

    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    public byte getOrder() {
        return asByte("Order");
    }
}
