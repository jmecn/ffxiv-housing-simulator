package ffxiv.housim.saintcoinach.xiv.entity;

/**
 * Interface for objects that have specific locations.
 */
public interface ILocatable {
    /**
     * Gets the locations of the current object.
     * @return The locations of the current object.
     */
    Iterable<ILocation> getLocations();
}
