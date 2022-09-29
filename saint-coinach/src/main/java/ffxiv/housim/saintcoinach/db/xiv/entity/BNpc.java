package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.xiv.collections.BNpcCollection;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a combination of {@link BNpcName} and {@link BNpcBase}.
 *
 * This class relies on information provided by Libra Eorzea.
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class BNpc implements ILocatable, IItemSource {

    private final ffxiv.housim.saintcoinach.db.libra.BNpcName libraRow;
    private List<BNpcLocation> locations;
    private List<Item> items;
    private List<InstanceContent> instanceContents;

    /**
     * The key of the current BNpcName.
     *
     * This value is ((base.getKey() * 10000000000) + name.getKey()).
     */
    @Getter
    private final long key;

    /**
     * The {@link BNpcBase} of current BNpcName.
     */
    @Getter
    private final BNpcBase base;

    /**
     * The {@link BNpcName} of current BNpcName.
     */
    @Getter
    private final BNpcName name;

    @Getter
    private final BNpcCollection collection;

    public BNpc(BNpcCollection collection, ffxiv.housim.saintcoinach.db.libra.BNpcName libra) {
        this.collection = collection;
        this.libraRow = libra;
        this.key = libra.getKey();
        this.base = collection.getBaseSheet().get((int) libra.getBaseKey());
        this.name = collection.getNameSheet().get((int) libra.getNameKey());
    }

    public String getFullKey() {
        return base.getKey() + "-" + name.getKey();
    }

    @Override
    public List<BNpcLocation> getLocations() {
        if (locations == null) {
            buildLocations(libraRow);
        }
        return locations;
    }

    @Override
    public Collection<Item> getItems() {
        if (items == null) {
            buildItems(libraRow);
        }
        return items;
    }

    public Collection<InstanceContent> getInstanceContents() {
        if (instanceContents == null) {
            buildInstanceContents(libraRow);
        }
        return instanceContents;
    }

    private void buildLocations(ffxiv.housim.saintcoinach.db.libra.BNpcName libraRow) {
        locations = Collections.emptyList();
        if (libraRow == null) {
            return;
        }
        throw new UnsupportedOperationException();
    }

    private void buildItems(ffxiv.housim.saintcoinach.db.libra.BNpcName libraRow) {
        items = Collections.emptyList();
        if (libraRow == null) {
            return;
        }
        throw new UnsupportedOperationException();
    }

    private void buildInstanceContents(ffxiv.housim.saintcoinach.db.libra.BNpcName libraRow) {
        instanceContents = Collections.emptyList();
        if (libraRow == null) {
            return;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return getName().getSingular();
    }
}
