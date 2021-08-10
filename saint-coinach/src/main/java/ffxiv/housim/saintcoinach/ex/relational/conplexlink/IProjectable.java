package ffxiv.housim.saintcoinach.ex.relational.conplexlink;

import ffxiv.housim.saintcoinach.ex.IRow;

public interface IProjectable {
    Object project(IRow row);
}