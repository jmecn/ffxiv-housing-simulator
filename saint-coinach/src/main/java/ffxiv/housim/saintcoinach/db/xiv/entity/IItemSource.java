package ffxiv.housim.saintcoinach.db.xiv.entity;

import java.util.Collection;

/**
 * Interface for objects from which {@link Item}s can be obtained.
 */
public interface IItemSource {

    /**
     * Gets the {@link Item}s that can be obtained from the current object.
     *
     * @return The {@link Item}s that can be obtained from the current object.
     */
    Collection<? extends Item> getItems();
}
