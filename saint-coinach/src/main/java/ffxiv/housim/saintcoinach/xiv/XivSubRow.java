package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.IRow;
import ffxiv.housim.saintcoinach.ex.SubRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;

public class XivSubRow extends XivRow implements IXivSubRow{

    private SubRow sourceSubRow;

    public XivSubRow(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
        this.sourceSubRow = (SubRow) sourceRow;
    }

    public String getFullKey() {
        return sourceSubRow.getFullKey();
    }

    @Override
    public IRow getParentRow() {
        return sourceSubRow.getParentRow();
    }

    @Override
    public int getParentKey() {
        return sourceSubRow.getParentRow().getKey();
    }
}
