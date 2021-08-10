package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.ex.IRow;

public class IdentityProjection implements IProjectable {
    public Object project(IRow row) {
        return row;
    }
}