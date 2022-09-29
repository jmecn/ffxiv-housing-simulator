package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.collections.ENpcCollection;
import ffxiv.housim.saintcoinach.math.Vector2;
import ffxiv.housim.saintcoinach.utils.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class ENpc implements ILocatable, IQuantifiableXivString{
    private ENpcBase base;
    private ENpcResident resident;
    private List<? extends ILocation> locations;

    @Getter
    private int key;
    @Getter
    private ENpcCollection collection;

    public ENpc(ENpcCollection collection, int key) {
        this.collection = collection;
        this.key = key;
    }

    public ENpcBase getBase() {
        if (base == null) {
            base = collection.getBaseSheet().get(key);
        }
        return base;
    }

    public ENpcResident getResident() {
        if (resident == null) {
            resident = collection.getResidentSheet().get(key);
        }
        return resident;
    }

    @Override
    public String getSingular() {
        return getResident().getSingular();
    }
    @Override
    public String getPlural() {
        return getResident().getPlural();
    }
    public String getTitle() {
        return getResident().getTitle();
    }
    @Override
    public Collection<? extends ILocation> getLocations() {
        if (locations == null) {
            locations = buildLocations();
        }
        return locations;
    }

    private List<Level> buildLevels() {
        List<Level> levels = new ArrayList<>();
        IXivSheet<Level> sheet = collection.getCollection().getSheet(Level.class);
        for (Level level : sheet) {
            if (level.getKey() == key) {
                levels.add(level);
            }
        }
        return levels;
    }

    private List<? extends ILocation> buildLocations() {
        List<Level> levelLocations = buildLevels();

        XivCollection coll = collection.getCollection();
        if (!coll.isLibraAvailable()) {
            return levelLocations;
        }

        ffxiv.housim.saintcoinach.db.libra.ENpcResident libraENpc = coll.getLibra().getENpcResidents().stream()
                .filter(it -> it.getKey() == this.key).findFirst().orElse(null);
        if (libraENpc == null) {
            return levelLocations;
        }

        List<ILocation> locations = new ArrayList<>(levelLocations);

        IXivSheet<PlaceName> placeNames = coll.getSheet(PlaceName.class);

        if (libraENpc.getCoordinates() != null) {
            for (Pair<Integer, List<Vector2>> coord : libraENpc.getCoordinates()) {
                PlaceName placeName = null;
                for (PlaceName it : placeNames) {
                    if (it.getKey() == coord.getKey()) {
                        placeName = it;
                        break;
                    }
                }

                if (placeName == null) {
                    continue;
                }
                for (Vector2 c : coord.getVal()) {
                    // Only add if no Level exists in the same area.
                    boolean isExist = false;
                    for (Level l : levelLocations) {
                        XivMap map = l.getMap();

                        if (Math.abs(l.getMapX() - c.x) < 1
                                && Math.abs(l.getMapY() - c.y) < 1
                                && (map.getLocationPlaceName() == placeName
                                    || map.getPlaceName() == placeName
                                    || map.getRegionPlaceName() == placeName)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        locations.add(new GenericLocation(placeName, c.x, c.y));
                    }
                }
            }
        }

        return locations;
    }

    @Override
    public String toString() {
        return getResident().getSingular();
    }
}
