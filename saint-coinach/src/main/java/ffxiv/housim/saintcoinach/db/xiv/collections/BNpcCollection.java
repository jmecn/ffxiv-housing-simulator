package ffxiv.housim.saintcoinach.db.xiv.collections;

import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.BNpc;
import ffxiv.housim.saintcoinach.db.xiv.entity.BNpcBase;
import ffxiv.housim.saintcoinach.db.xiv.entity.BNpcName;
import ffxiv.housim.saintcoinach.db.xiv.entity.ItemBase;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class BNpcCollection implements Iterable<BNpc> {

    private Map<Long, BNpc> innerDictionary = new HashMap<>();
    private Collection<ffxiv.housim.saintcoinach.db.libra.BNpcName> libraEntries;

    @Getter
    private XivCollection collection;
    @Getter
    private IXivSheet<BNpcBase> baseSheet;
    @Getter
    private IXivSheet<BNpcName> nameSheet;

    public BNpcCollection(XivCollection collection) {
        this.collection = collection;
        this.baseSheet = collection.getSheet(BNpcBase.class);
        this.nameSheet = collection.getSheet(BNpcName.class);
    }

    @Override
    public Iterator<BNpc> iterator() {
        return null;
    }
}
