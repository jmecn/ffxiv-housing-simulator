package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.Column;
import ffxiv.housim.saintcoinach.ex.Header;
import ffxiv.housim.saintcoinach.ex.relational.definition.PositionedDataDefinition;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;

public class RelationalColumn extends Column {

    private boolean hasDefinition;
    private PositionedDataDefinition definition;

    private final RelationalHeader header;

    /**
     * Initializes a new instance of the {@link Column} class.
     *
     * @param header The {@link Header} of the EX file the column is in.
     * @param index  The index of the column inside the EX file.
     * @param type   The column value type
     * @param offset The column offset
     */
    public RelationalColumn(RelationalHeader header, int index, int type, int offset) {
        super(header, index, type, offset);
        this.header = header;
    }

    public PositionedDataDefinition getDefinition() {
        if (hasDefinition) {
            return definition;
        }

        if (header.getSheetDefinition() != null) {
            PositionedDataDefinition definition = header.getSheetDefinition().tryGetDefinition(index);
            if (definition != null) {
                this.definition = definition;
            }
        }

        hasDefinition = true;
        return definition;
    }

    public String getName() {
        if (header.getSheetDefinition() == null) {
            return null;
        }

        return header.getSheetDefinition().getColumnName(index);
    }

    @Override
    public String getValueType() {
        SheetDefinition def = header.getSheetDefinition();
        if (def == null) {
            return super.getValueType();
        }

        String t = def.getValueTypeName(index);

        return t == null ? super.getValueType() : t;
    }
}
