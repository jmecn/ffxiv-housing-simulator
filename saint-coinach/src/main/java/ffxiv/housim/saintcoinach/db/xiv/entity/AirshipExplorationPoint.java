package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class AirshipExplorationPoint extends XivRow {
    public AirshipExplorationPoint(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }
    
    public String getShortName() {
        return asString("Name{Short}");
    }

    public int getRequiredLevel() {
        return asInt32("RequiredLevel");
    }
    
    public int getRequiredFuel() {
        return asInt32("RequiredFuel");
    }

    /**
     * Return duration in minutes.
     * @return duration in minutes
     */
    public int getDuration() {
        return asInt32("Duration<min>");
    }

    public int getExpReward() {
        return asInt32("ExpReward");
    }
}
