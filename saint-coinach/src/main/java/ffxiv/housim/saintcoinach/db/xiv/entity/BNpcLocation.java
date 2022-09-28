package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.xiv.entity.map.PlaceName;
import lombok.Getter;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class BNpcLocation implements ILocation {

    @Getter
    private PlaceName regionPlaceName;
    @Getter
    private PlaceName placeName;
    @Getter
    private int minimumLevel;
    @Getter
    private int maximumLevel;

    public BNpcLocation(PlaceName regionPlaceName, PlaceName placeName, int minimumLevel, int maximumLevel) {
        this.regionPlaceName = regionPlaceName;
        this.placeName = placeName;
        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
    }

    @Override
    public double getMapX() {
        return Double.NaN;
    }

    @Override
    public double getMapY() {
        return Double.NaN;
    }
}
