package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.db.ex.IRow;

public class IdentityProjection implements IProjectable {
    public Object project(IRow row) {
        return row;
    }
}