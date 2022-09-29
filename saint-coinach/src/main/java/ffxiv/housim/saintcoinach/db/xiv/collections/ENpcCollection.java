package ffxiv.housim.saintcoinach.db.xiv.collections;

import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.*;
import lombok.Getter;

import java.util.*;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class ENpcCollection implements Iterable<ENpc> {

    private Map<Long, ENpc> inner = new HashMap<>();
    private Map<Long, List<ENpc>> eNpcDataMap;

    @Getter
    private XivCollection collection;
    @Getter
    private IXivSheet<ENpcBase> baseSheet;
    @Getter
    private IXivSheet<ENpcResident> residentSheet;

    public ENpcCollection(XivCollection collection) {
        this.collection = collection;
        this.baseSheet = collection.getSheet(ENpcBase.class);
        this.residentSheet = collection.getSheet(ENpcResident.class);
    }

    @Override
    public Iterator<ENpc> iterator() {
        return null;
    }
}
