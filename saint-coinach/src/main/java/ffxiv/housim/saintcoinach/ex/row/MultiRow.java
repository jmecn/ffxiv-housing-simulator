package ffxiv.housim.saintcoinach.ex.row;

import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.sheet.IMultiSheet;
import lombok.Getter;

import java.util.List;

public class MultiRow implements IMultiRow {
    @Getter
    private IMultiSheet sheet;
    @Getter
    private int key;

    public MultiRow(IMultiSheet sheet, int key) {
        this.sheet = sheet;
        this.key = key;
    }

    @Override
    public Object get(int columnIndex) {
        return sheet.getActiveSheet().get(key).get(columnIndex);
    }

    @Override
    public Object get(int columnIndex, Language language) {
        return sheet.getLocalisedSheet(language).get(columnIndex);
    }

    @Override
    public Object getRaw(int columnIndex) {
        return sheet.getActiveSheet().get(key).getRaw(columnIndex);
    }

    @Override
    public Object getRaw(int columnIndex, Language language) {
        return sheet.getLocalisedSheet(language).get(key).getRaw(columnIndex);
    }

    @Override
    public List<Object> getColumnValues() {
        return sheet.getActiveSheet().get(key).getColumnValues();
    }
}
