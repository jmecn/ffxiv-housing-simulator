package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IMultiSheet;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.MultiRow;

public class RelationalMultiRow extends MultiRow implements IRelationalMultiRow {

    public RelationalMultiRow(IMultiSheet sheet, int key) {
        super(sheet, key);
    }

    public IRelationalMultiSheet getSheet() {
        return (IRelationalMultiSheet) super.getSheet();
    }

    @Override
    public Object getDefaultValue() {
        return getSheet().getActiveSheet().get(getKey()).getDefaultValue();
    }

    @Override
    public Object get(String columnName, Language language) {
        return getSheet().getLocalisedSheet(language).get(getKey()).get(columnName);
    }

    @Override
    public Object getRaw(String columnName, Language language) {
        return getSheet().getLocalisedSheet(language).get(getKey()).getRaw(columnName);
    }

    @Override
    public Object get(String columnName) {
        return getSheet().getActiveSheet().get(getKey()).get(columnName);
    }

    @Override
    public Object getRaw(String columnName) {
        return getSheet().getActiveSheet().get(getKey()).getRaw(columnName);
    }

    @Override
    public String toString() {
        RelationalColumn defCol = getSheet().getHeader().getDefaultColumn();
        return defCol == null
                ? String.format("%s#%d", getSheet().getHeader().getName(), getKey())
                : String.format("%s", get(defCol.getIndex()));
    }
}