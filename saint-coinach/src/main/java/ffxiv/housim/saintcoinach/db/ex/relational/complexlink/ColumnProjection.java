package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.IRow;

public class ColumnProjection implements IProjectable {
    public String projectedColumnName;

    public ColumnProjection(String projectedColumnName) {
        this.projectedColumnName = projectedColumnName;
    }

    public Object project(IRow row) {
        IRelationalRow relationalRow = (IRelationalRow) row;
        return relationalRow.get(projectedColumnName);
    }
}