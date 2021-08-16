package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.xiv.entity.Item;

/**
 * Interface for objects from which {@link Item}s can be obtained.
 */
public interface IItemSource {

    /**
     * Gets the {@link Item}s that can be obtained from the current object.
     *
     * @return The {@link Item}s that can be obtained from the current object.
     */
    Iterable<Item> getItems();
}
