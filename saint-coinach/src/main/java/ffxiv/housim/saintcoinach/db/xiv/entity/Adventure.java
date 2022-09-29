package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;

import java.util.Collection;
import java.util.Collections;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class Adventure extends XivRow implements ILocatable {
    public Adventure(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public Level getLevel() {
        return as(Level.class);
    }

    public Emote getEmote() {
        return as(Emote.class);
    }

    public int getMinTime() {
        return asInt32("MinTime");
    }

    public int getMaxTime() {
        return asInt32("MaxTime");
    }

    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    public ImageFile getListIcon() {
        return asImage("Icon{List}");
    }

    public ImageFile getDiscoveredIcon() {
        return asImage("Icon{Discovered}");
    }

    public ImageFile getUndiscoveredIcon() {
        return asImage("Icon{Undiscovered}");
    }

    public boolean isInitial() {
        return asBoolean("IsInitial");
    }

    public String getName() {
        return asString("Name");
    }

    public String getImpression() {
        return asString("Impression");
    }

    public String getDescription() {
        return asString("Description");
    }

    @Override
    public Collection<ILocation> getLocations() {
        return Collections.singletonList(getLevel());
    }

    @Override
    public String toString() {
        return getName();
    }
}
