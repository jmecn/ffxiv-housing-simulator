package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.IDataSheet;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RelationalDataIndex<T extends IDataRow> {

    private Map<Integer, T> indexedRows;

    private IDataSheet<T> sourceSheet;
    private Column indexColumn;

    public RelationalDataIndex(IDataSheet<T> sourceSheet, Column indexColumn) {
        this.sourceSheet = sourceSheet;
        this.indexColumn = indexColumn;
        this.indexedRows = new HashMap<>();

        for (T row : sourceSheet) {
            int index = indexColumn.getIndex();
            int key = (int)row.getRaw(index);
            indexedRows.put(key, row);
        }
    }

    public T get(int key) {
        T row = indexedRows.get(key);
        if (row != null) {
            indexedRows.put(key, row);
        } else {
            log.warn("Indexed row not found, key:{}", key);
        }

        return row;
    }
}
