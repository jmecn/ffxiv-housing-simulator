package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.collections.BNpcCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class BNpcName extends XivRow implements ILocatable, IItemSource, IQuantifiableXivString {

    private List<BNpc> bNpcs;

    private List<ILocation> locations;
    private List<Item> items;
    private List<InstanceContent> instanceContents;

    public BNpcName(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public List<BNpc> getBNpcs() {
        if (bNpcs == null) {
            if (!getSheet().getCollection().isLibraAvailable()) {
                bNpcs = Collections.emptyList();
                return bNpcs;
            }

            BNpcCollection collection = getSheet().getCollection().getBNpcs();
            // TODO bNpcs = collection.where(i -> i.getName() == this);
            throw new UnsupportedOperationException();
        }
        return bNpcs;
    }

    @Override
    public Collection<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>();
            getBNpcs().forEach(it -> items.addAll(it.getItems()));
        }
        return items;
    }

    @Override
    public Collection<ILocation> getLocations() {
        if (locations == null) {
            locations = new ArrayList<>();
            getBNpcs().forEach(it -> locations.addAll(it.getLocations()));
        }
        return locations;
    }

    public Collection<InstanceContent> getInstanceContents() {
        if (instanceContents == null) {
            instanceContents = new ArrayList<>();
            getBNpcs().forEach(it -> instanceContents.addAll(it.getInstanceContents()));
        }
        return instanceContents;
    }

    @Override
    public String getSingular() {
        return asString("Singular");
    }

    @Override
    public String getPlural() {
        if (getSheet().getCollection().getActiveLanguage() == Language.Japanese) {
            return getSingular();
        } else {
            return asString("Plural");
        }
    }
}
