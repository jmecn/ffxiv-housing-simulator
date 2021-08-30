package ffxiv.housim.saintcoinach.db.xiv.entity.level;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivSubRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/30
 */
public class WeatherGroup extends XivSubRow {
    public WeatherGroup(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public WeatherRate getWeatherRate() {
        return as(WeatherRate.class);
    }
}
