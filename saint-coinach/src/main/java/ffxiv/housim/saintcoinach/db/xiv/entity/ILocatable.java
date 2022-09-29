package ffxiv.housim.saintcoinach.db.xiv.entity;

import java.util.Collection;

/**
 * Interface for objects that have specific locations.
 */
public interface ILocatable {
    /**
     * Gets the locations of the current object.
     * @return The locations of the current object.
     */
    Collection<? extends ILocation> getLocations();
}
