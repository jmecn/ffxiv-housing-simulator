package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

/**
 * Class representing a location in the game.
 */
public class Level extends XivRow implements ILocation{
    public Level(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public float getX() {
        return asSingle("X");
    }

    public float getY() {
        return asSingle("Y");
    }

    public float getZ() {
        return asSingle("Z");
    }

    @Override
    public double getMapX() {
        XivMap map = getMap();
        return map.toMapCoordinate3d(getX(), map.getOffsetX());
    }

    @Override
    public double getMapY() {
        XivMap map = getMap();
        return map.toMapCoordinate3d(getZ(), map.getOffsetY());
    }

    public float getYaw() {
        return asSingle("Yaw");
    }

    public float getRadius() {
        return asSingle("Radius");
    }

    /**
     * Gets the LgbEntryType of the current location.
     * @return The LgbEntryType of the current location.
     */
    public int getType() {
        return asInt32("Type");
    }

    @Override
    public PlaceName getPlaceName() {
        return getMap().getPlaceName();
    }

    public XivMap getMap() {
        return as(XivMap.class);
    }
}
