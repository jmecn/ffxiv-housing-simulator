package ffxiv.housim.saintcoinach.db.xiv.entity;

/**
 * Interface for listings of shops.
 */
public interface IShopListing {

    /**
     * Gets the rewards of the current listing.
     * @return The rewards of the current listing.
     */
    Iterable<IShopListingItem> getRewards();

    /**
     * Gets the costs of the current listing.
     * @return The costs of the current listing.
     */
    Iterable<IShopListingItem> getCosts();

    /**
     * Gets the shops offering the current listing.
     * @return The shops offering the current listing.
     */
    Iterable<IShop> getShops();
}
