package ffxiv.housim.saintcoinach.db.xiv.collections;

import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.Action;
import ffxiv.housim.saintcoinach.db.xiv.entity.CraftAction;
import ffxiv.housim.saintcoinach.db.xiv.entity.ClassJobActionBase;
import lombok.Getter;

import java.util.Iterator;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class ClassJobActionCollection implements Iterable<ClassJobActionBase> {

    @Getter
    private XivCollection collection;
    @Getter
    private IXivSheet<Action> actionSheet;
    @Getter
    private IXivSheet<CraftAction> craftActionSheet;

    public ClassJobActionCollection(XivCollection collection) {
        this.collection = collection;
        this.actionSheet = collection.getSheet(Action.class);
        this.craftActionSheet = collection.getSheet(CraftAction.class);
    }

    @Override
    public Iterator<ClassJobActionBase> iterator() {
        return null;
    }
}
