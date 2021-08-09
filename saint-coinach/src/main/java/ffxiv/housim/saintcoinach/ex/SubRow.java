package ffxiv.housim.saintcoinach.ex;

public class SubRow extends DataRowBase {

    private IDataRow parentRow;

    public SubRow(IDataRow parent, int key, int offset) {
        super(parent.getSheet(), key, offset);

        parentRow = parent;
    }

    // TODO IRelationalRow members
}
