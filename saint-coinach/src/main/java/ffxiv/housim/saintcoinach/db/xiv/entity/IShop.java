package ffxiv.housim.saintcoinach.db.xiv.entity;

/**
 * Interface for shops.
 */
public interface IShop extends IItemSource {

    /**
     * Gets the key of the current shop.
     * @return The key of the current shop.
     */
    int getKey();

    /**
     * Gets the name of the current shop.
     * @return The name of the current shop.
     */
    String getName();

    /**
     * Gets the {@link ENpcs} offering the current shop.
     * @return The {@link ENpcs} offering the current shop.
     */
    Iterable<ENpcs> getENpcs();

    /**
     * Gets the listings of the current shop.
     * @return The listings of the current shop.
     */
    Iterable<IShopListing> getShopListings();
}
