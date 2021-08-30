package ffxiv.housim.saintcoinach.db.xiv.entity.level;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.XivName;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.scene.terrain.Territory;
import ffxiv.housim.saintcoinach.texture.ImageFile;

import java.lang.ref.WeakReference;

// TODO need implements

/**
 * Class representing a map.
 */
@XivName("Map")
public class XivMap extends XivRow {
    private WeakReference<ImageFile> mediumImage;
    private WeakReference<ImageFile> mediumImageMask;
    private WeakReference<ImageFile> smallImage;
    private WeakReference<ImageFile> smallImageMask;

    public XivMap(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public byte getIndex() {
        return asByte("MapIndex");
    }

    /**
     * Gets the identifier string of the current map.
     * @return The identifier string of the current map.
     */
    public String getId() {
        return asString("Id");
    }

    /**
     * Gets the hierarchy level of the current map.
     * @return The hierarchy level of the current map.
     */
    public int getHierarchy() {
        return asInt32("Hierarchy");
    }

    /**
     * Gets the MapMarker parent key range of the current map.
     * @return The MapMarker parent key range of the current map.
     */
    public int getMapMarkerRange() {
        return asInt32("MapMarkerRange");
    }

    /**
     * Gets the size factor of the current map.
     *
     * Base map size 41 units, the value of <code>SizeFactor</code> indicates by how much to divide this to get the size of the current map.
     * @return The size of the current map.
     */
    public int getSizeFactor() {
        return asInt32("SizeFactor");
    }

    /**
     * Gets the X value offset of the current map.
     * @return The X value offset of the current map.
     */
    public int getOffsetX() {
        return asInt32("Offset{X}");
    }

    /**
     * Gets the Y value offset of the current map.
     * @return The Y value offset of the current map.
     */
    public int getOffsetY() {
        return asInt32("Offset{Y}");
    }

    /**
     * Gets the {@link PlaceName} of the region the current map is in.
     * @return The {@link PlaceName} of the region the current map is in.
     */
    public PlaceName getRegionPlaceName() {
        return as(PlaceName.class, "PlaceName{Region}");
    }

    /**
     * Gets the {@link PlaceName} of current map is in.
     * @return The {@link PlaceName} of current map is in.
     */
    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    /**
     * Gets the {@link PlaceName} of the more specific location the current map is in.
     * @return The {@link PlaceName} of the more specific location the current map is in.
     */
    public PlaceName getLocationPlaceName() {
        return as(PlaceName.class, "PlaceName{Sub}");
    }

    /**
     * Gets the {@link TerritoryType} for the current map.
     * @return
     */
    public TerritoryType getTerritoryType() {
        return as(TerritoryType.class);
    }

    public Territory getTerritory() {
        TerritoryType type = getTerritoryType();

        if (type == null || type.getKey() == 0) {
            return null;
        }

        Territory t = new Territory(type);
        if (t.getTerrain() == null && t.getLgbFiles().size() == 0) {
            return null;
        }

        return t;
    }
    public ImageFile getMediumImage() {
        if (mediumImage != null && mediumImage.get() != null) {
            return mediumImage.get();
        }

        ImageFile image = buildImage("m", "");
        if (image != null) {
            mediumImage = new WeakReference<>(image);
        }
        return image;
    }

    public ImageFile getMediumImageMask() {
        if (mediumImageMask != null && mediumImageMask.get() != null) {
            return mediumImageMask.get();
        }

        ImageFile image = buildImage("m", "m");
        if (image != null) {
            mediumImageMask = new WeakReference<>(image);
        }
        return image;
    }

    public ImageFile getSmallImage() {
        if (smallImage != null && smallImage.get() != null) {
            return smallImage.get();
        }

        ImageFile image = buildImage("s", "");
        if (image != null) {
            smallImage = new WeakReference<>(image);
        }
        return image;
    }

    public ImageFile getSmallImageMask() {
        if (smallImageMask != null && smallImageMask.get() != null) {
            return smallImageMask.get();
        }

        ImageFile image = buildImage("s", "m");
        if (image != null) {
            smallImageMask = new WeakReference<>(image);
        }
        return image;
    }

    private ImageFile buildImage(String size, String mask) {
        String format = "ui/map/%s/%s%s_%s.tex";
        String id = getId();
        if (id == null || id.isEmpty()) {
            return null;
        }

        String fileName = id.replaceAll("/", "");
        PackCollection packs = getSheet().getCollection().getPackCollection();

        String filePath = String.format(format, id, fileName, mask, size);

        return (ImageFile) packs.tryGetFile(filePath);
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
