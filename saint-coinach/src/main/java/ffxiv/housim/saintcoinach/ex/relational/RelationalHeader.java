package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

public class RelationalHeader extends Header {

    @Getter
    private RelationalExCollection collection;
    @Getter
    private RelationalColumn[] columns;

    public RelationalHeader(RelationalExCollection collection, String name, PackFile file) {
        super(collection, name, file);
        this.collection = collection;
    }

    public RelationalColumn getDefaultColumn() {

        // TODO
        return null;
    }

    public SheetDefinition getSheetDefinition() {
        SheetDefinition def = collection.getDefinition().tryGetSheet(getName());
        return def;
    }

    public RelationalColumn findColumn(String name) {
        return null;
    }
}
