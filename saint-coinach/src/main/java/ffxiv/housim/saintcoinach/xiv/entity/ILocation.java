package ffxiv.housim.saintcoinach.xiv.entity;

/**
 * Interface for objects defining a location in a zone (in map-coordinates).
 */
public interface ILocation {

    /**
     * Gets the x-coordinate of the current object.
     * @return The x-coordinate of the current object.
     */
    double getMapX();

    /**
     * Gets the y-coordinate of the current object.
     * @return The y-coordinate of the current object.
     */
    double getMapY();

    /**
     * Gets the {@link PlaceName} of the current object's location.
     * @return The {@link PlaceName} of the current object's location.
     */
    PlaceName getPlaceName();
}
