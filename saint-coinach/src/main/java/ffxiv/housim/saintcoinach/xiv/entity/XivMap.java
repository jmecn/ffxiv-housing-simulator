package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;
import ffxiv.housim.saintcoinach.xiv.XivName;

// TODO need implements
@XivName("Map")
public class XivMap extends XivRow {
    public XivMap(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public byte getIndex() {
        return asByte("MapIndex");
    }

    public String getId() {
        return asString("Id");
    }

    public int getHierarchy() {
        return asInt32("Hierarchy");
    }

    public int getMapMarkerRange() {
        return asInt32("MapMarkerRange");
    }

    public int getSizeFactor() {
        return asInt32("SizeFactor");
    }

    public int getOffsetX() {
        return asInt32("Offset{X}");
    }

    public int getOffsetY() {
        return asInt32("Offset{Y}");
    }

    public PlaceName getRegionPlaceName() {
        return as(PlaceName.class, "PlaceName{Region}");
    }

    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    public PlaceName getLocationPlaceName() {
        return as(PlaceName.class, "PlaceName{Sub}");
    }

    public double toMapCoordinate2d(int value, int offset) {
        double c = getSizeFactor() / 100.0;
        double offsetValue = value + offset;
        return (41.0 / c) * (offsetValue / 2048.0) + 1;
    }

    public double toMapCoordinate3d(double value, int offset) {
        double c = getSizeFactor() / 100.0;
        double offsetValue = (value + offset) * c;
        return ((41.0 / c) * ((offsetValue + 1024.0) / 2048.0)) + 1;
    }

    @Override
    public String toString() {
        return getId();
    }
}
