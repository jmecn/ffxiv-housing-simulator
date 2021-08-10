package ffxiv.housim.saintcoinach.ex.relational.complexlink;

import ffxiv.housim.saintcoinach.ex.IRow;

public interface IProjectable {
    Object project(IRow row);
}