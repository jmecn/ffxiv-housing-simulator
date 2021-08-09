package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.MultiSheet;

public class RelationalMultiSheet<TData extends IRelationalDataRow> extends MultiSheet<TData> {

    public RelationalMultiSheet(RelationalExCollection collection, RelationalHeader header, Class<TData> dataRowClass) {
        super(collection, header, dataRowClass);
    }
}
