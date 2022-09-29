package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import lombok.Getter;

/**
 * Class containing data about a BNpc.
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class BNpcData {
    @Getter
    private final BNpcBase base;
    @Getter
    private final BNpcName name;

    public BNpcData(XivCollection collection, int baseKey, int nameKey) {
        base = collection.getSheet(BNpcBase.class).get(baseKey);
        name = collection.getSheet(BNpcName.class).get(nameKey);
    }
}
