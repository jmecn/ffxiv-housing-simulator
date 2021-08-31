package ffxiv.housim.saintcoinach.db.xiv.entity.map;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * Class enabling the prediction of weather in Eorzea.
 */
// TODO
public class WeatherRate extends XivRow {
    public WeatherRate(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
