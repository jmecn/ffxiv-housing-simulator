package ffxiv.housim.saintcoinach.db.xiv.entity;

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
