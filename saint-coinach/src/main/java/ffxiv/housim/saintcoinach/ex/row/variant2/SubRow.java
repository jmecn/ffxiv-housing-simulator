package ffxiv.housim.saintcoinach.ex.row.variant2;

import ffxiv.housim.saintcoinach.ex.row.DataRowBase;
import ffxiv.housim.saintcoinach.ex.row.IDataRow;

public class SubRow extends DataRowBase {

    private IDataRow parentRow;

    public SubRow(IDataRow parent, int key, int offset) {
        super(parent.getSheet(), key, offset);

        parentRow = parent;
    }

    // TODO IRelationalRow members
}
