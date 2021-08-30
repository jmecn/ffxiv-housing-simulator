package ffxiv.housim.saintcoinach.db.xiv.entity.level;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a territory (zone).
 */
public class TerritoryType extends XivRow {
    /**
     * Mappings of special weather rates.
     */
    private static Map<Integer, WeatherRate> weatherGroup;

    /**
     * {@link WeatherRate} of the current territory.
     */
    private WeatherRate weatherRate;

    private Map<Integer, XivMap> mapsByIndex;

    public TerritoryType(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    /**
     * Gets the name of the current territory.
     * @return The name of the current territory.
     */
    public String getName() {
        return asString("Name");
    }

    /**
     * Gets the identifier used for the current territory.
     * @return The identifier used for the current territory.
     */
    public String getBg() {
        return asString("Bg");
    }

    /**
     * Gets the {@link XivMap} of the current territory.
     * @return The {@link XivMap} of the current territory.
     */
    public XivMap getMap() {
        return as(XivMap.class);
    }

    /**
     * Gets the {@link PlaceName} of the current territory.
     * @return The {@link PlaceName} of the current territory.
     */
    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    /**
     * Gets the {@link PlaceName} of the region the current map is in.
     * @return The {@link PlaceName} of the region the current map is in.
     */
    public PlaceName getRegionPlaceName() {
        return as(PlaceName.class, "PlaceName{Region}");
    }

    /**
     * Gets the {@link PlaceName} of the zone the current territory is in.
     * @return The {@link PlaceName} of the zone the current territory is in.
     */
    public PlaceName getZonePlaceName() {
        return as(PlaceName.class, "PlaceName{Zone}");
    }

    public WeatherRate getWeatherRate() {
        if (weatherRate != null) {
            return weatherRate;
        }

        if (weatherGroup == null) {
            buildWeatherGroups();
        }

        int rateKey = asInt32("WeatherRate");
        weatherRate = weatherGroup.get(rateKey);
        if (weatherRate == null) {
            weatherRate = getSheet().getCollection().getSheet(WeatherRate.class).get(rateKey);
            weatherGroup.put(rateKey, weatherRate);
        }

        return weatherRate;
    }

    public XivMap getRelatedMap(int index) {
        if (mapsByIndex == null) {
            buildMapIndex();
        }

        XivMap map = mapsByIndex.get(index);
        if (map != null) {
            return map;
        }

        // Fallback to the default map.  This may not be accurate.
        return getMap();
    }

    private Map<Integer, WeatherRate> buildWeatherGroups() {
        Map<Integer, WeatherRate> map = new HashMap<>();
        IXivSheet<WeatherGroup> sheet = getSheet().getCollection().getSheet2(WeatherGroup.class);
        for (WeatherGroup e : sheet) {
            if (e.getKey() != 0) {
                continue;
            }

            map.put(e.getParentRow().getKey(), e.getWeatherRate());
        }

        weatherGroup = map;
        return map;
    }

    private Map<Integer, XivMap> buildMapIndex() {
        IXivSheet<XivMap> maps = getSheet().getCollection().getSheet(XivMap.class);

        Map<Integer, XivMap> index = new HashMap<>();
        for (XivMap map : maps) {
            String mapId = map.getId();
            if (mapId == null || mapId.isEmpty()) {
                continue;
            }

            String mapIndex = mapId.substring(mapId.indexOf("/") + 1);
            Integer convertedIndex = Integer.parseInt(mapIndex);
            if (index.containsKey(convertedIndex)) {
                continue;// skip it for now
            }
            index.put(convertedIndex, map);
        }

        mapsByIndex = index;
        return index;
    }
}
