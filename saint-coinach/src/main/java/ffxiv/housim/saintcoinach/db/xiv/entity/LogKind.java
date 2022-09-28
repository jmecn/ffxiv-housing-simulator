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
public class LogKind extends XivRow {
    public LogKind(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getFormat() {
        return asString("Format");
    }

    public String getName() {
        return asString("Name");
    }

    public String getExample() {
        return asString("Example");
    }

    @Override
    public String toString() {
        return getName();
    }
}
