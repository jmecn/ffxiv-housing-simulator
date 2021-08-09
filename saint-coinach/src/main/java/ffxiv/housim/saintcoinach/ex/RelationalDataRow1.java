package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalDataRow;
import ffxiv.housim.saintcoinach.ex.relational.IRelationalDataSheet;

public class RelationalDataRow1 extends DataRow1 implements IRelationalDataRow {
    public RelationalDataRow1(IDataSheet sheet, int key, int offset) {
        super(sheet, key, offset);
    }

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
