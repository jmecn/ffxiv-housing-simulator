package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalDataRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalDataSheet;

public class RelationalDataRow2 extends DataRow2 implements IRelationalDataRow {
    protected RelationalDataRow2(IDataSheet sheet, int key, int offset) {
        super(sheet, key, offset);
    }

    @Override
    public IRelationalDataSheet getSheet() {
        return (IRelationalDataSheet) super.getSheet();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public Object get(String columnName) {
        return null;
    }

    @Override
    public Object getRaw(String columnName) {
        return null;
    }
}
