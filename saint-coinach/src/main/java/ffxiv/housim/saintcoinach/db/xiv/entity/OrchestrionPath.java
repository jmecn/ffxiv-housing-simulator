package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class OrchestrionPath extends XivRow {
    public OrchestrionPath(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getFile() {
        return asString("File");
    }

    @Override
    public String toString() {
        return getFile();
    }
}
