package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class RelationalHeader extends Header {

    @Getter
    private RelationalExCollection collection;
    @Getter
    private RelationalColumn[] columns;

    public RelationalHeader(RelationalExCollection collection, String name, PackFile file) {
        super(collection, name, file);
        this.collection = collection;

        this.columns = Arrays.stream(super.getColumns())//
                .map(it -> (RelationalColumn) it)//
                .toArray(RelationalColumn[]::new);
    }

    public RelationalColumn getDefaultColumn() {

        SheetDefinition def = getSheetDefinition();
        if (def == null) {
            return null;
        }

        Integer i = def.getDefaultColumnIndex();
        return i != null ? getColumn(i.intValue()) : null;
    }

    public void setDefaultColumn(RelationalColumn value) {
        SheetDefinition def = getOrCreateSheetDefinition();
        def.setDefaultColumn(value == null ? null : value.getName());
    }

    public SheetDefinition getSheetDefinition() {
        return collection.getDefinition().tryGetSheet(getName());
    }

    @Override
    public RelationalColumn getColumn(int columnIndex) {
        return columns[columnIndex];
    }

    public SheetDefinition getOrCreateSheetDefinition() {
        return collection.getDefinition().getOrCreateSheet(getName());
    }

    @Override
    protected Column createColumn(int index, ByteBuffer buffer) {
        // 4 bytes
        int type = buffer.getShort();
        int offset = buffer.getShort();
        return new RelationalColumn(this, index, type, offset);
    }

    public RelationalColumn findColumn(String name) {
        SheetDefinition def = getSheetDefinition();
        if (def == null) {
            return null;
        }

        Integer i = def.findColumn(name);
        return i != null ? getColumn(i.intValue()) : null;
    }
}
