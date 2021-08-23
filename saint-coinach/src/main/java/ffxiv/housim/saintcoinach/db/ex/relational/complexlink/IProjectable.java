package ffxiv.housim.saintcoinach.db.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.db.ex.IRow;

public interface IProjectable {
    Object project(IRow row);
}