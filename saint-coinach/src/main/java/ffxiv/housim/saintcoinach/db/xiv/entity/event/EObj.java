package ffxiv.housim.saintcoinach.db.xiv.entity.event;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivName;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/31
 */
@XivName("EObj")
public class EObj extends XivRow {
    public EObj(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getSgbPath() {
        return asString("SgbPath");
    }
}