package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;

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