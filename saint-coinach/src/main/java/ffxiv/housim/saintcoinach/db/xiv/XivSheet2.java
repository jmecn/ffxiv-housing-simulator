package ffxiv.housim.saintcoinach.db.xiv;

import ffxiv.housim.saintcoinach.utils.Pair;
import ffxiv.housim.saintcoinach.db.ex.DataRowV2;
import ffxiv.housim.saintcoinach.db.ex.SubRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XivSheet2<T extends IXivSubRow> extends XivSheet<T> {

    private Map<Pair<Integer, Integer>, T> subRows = new ConcurrentHashMap<>();

    public XivSheet2(XivCollection collection, IRelationalSheet<T> source, Class<T> rowClass) {
        super(collection, source, rowClass);
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> sourceParentItr = source.iterator();

        return new Iterator<T>() {
            Iterator<SubRow> sourceSubItr;
            DataRowV2 currentParent;
            @Override
            public boolean hasNext() {
                if (sourceSubItr != null && sourceSubItr.hasNext()) {
                    return true;
                }

                if (!sourceParentItr.hasNext()) {
                    return false;
                }

                currentParent = (DataRowV2) sourceParentItr.next();

                sourceSubItr = currentParent.getSubRows().iterator();
                return sourceSubItr.hasNext();// should always be true
            }

            @Override
            public T next() {
                IRelationalRow sourceRow = sourceSubItr.next();
                Pair<Integer, Integer> key = new Pair<>(currentParent.getKey(), sourceRow.getKey());

                T t = subRows.get(key);
                if (t == null) {
                    try {
                        t = createRow(sourceRow);
                        subRows.put(key, t);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
                return t;
            }
        };
    }
}
