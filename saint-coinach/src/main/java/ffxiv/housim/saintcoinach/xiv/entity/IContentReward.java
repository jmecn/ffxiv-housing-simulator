package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.xiv.entity.Item;

/**
 * Interface for content rewards.
 */
public interface IContentReward {

    /**
     * Gets the rewarded {@link Item}.
     *
     * @return The rewarded {@link Item}.
     */
    Item getItem();

    /**
     * Gets the count for the current reward.
     *
     * @return The count for the current reward.
     */
    int getCount();
}
