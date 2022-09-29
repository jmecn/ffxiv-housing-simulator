package ffxiv.housim.saintcoinach.db.xiv.entity;

import lombok.Getter;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class GenericLocation implements ILocation {

    @Getter
    private PlaceName placeName;
    @Getter
    private double mapX;
    @Getter
    private double mapY;

    public GenericLocation(PlaceName placeName, double mapX, double mapY) {
        this.placeName = placeName;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
