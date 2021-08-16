package ffxiv.housim.saintcoinach.xiv.entity;

/**
 * Interface for items used in a {@link IShopListing}
 */
public interface IShopListingItem {

    /**
     * Gets the item of the current listing entry.
     * @return The item of the current listing entry.
     */
    Item getItem();

    /**
     * Gets the count for the current listing entry.
     * @return The count for the current listing entry.
     */
    int getCount();

    /**
     * Gets a value indicating whether the item is high-quality.
     * @return A value indicating whether the item is high-quality.
     */
    boolean isHq();

    /**
     * Gets the collectability rating for the item.
     * @return The collectability rating for the item.
     */
    int getCollectabilityRating();

    /**
     * Gets the {@link IShopListing} the current entry is for.
     * @return The {@link IShopListing} the current entry is for.
     */
    IShopListing getShopItem();
}
