package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.*;
import ffxiv.housim.saintcoinach.ex.relational.definition.RelationDefinition;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RelationalExCollection extends ExCollection {

    @Getter
    @Setter
    private RelationDefinition definition;

    public RelationalExCollection(PackCollection packCollection) {
        super(packCollection);
    }

    @Override
    protected RelationalHeader createHeader(String name, PackFile file) {
        return new RelationalHeader(this, name, file);
    }

    @Override
    protected ISheet<?> createSheet(Header header) {
        RelationalHeader relHeader = (RelationalHeader) header;
        if (relHeader.getVariant() == 1) {
            return createSheet(relHeader, RelationalDataRowV1.class);
        } else {
            return createSheet(relHeader, RelationalDataRowV2.class);
        }
    }

    @Override
    protected ISheet<?> createSheet(Header header, Class<? extends IDataRow> clazz) {
        RelationalHeader relHeader = (RelationalHeader) header;
        Class<? extends IRelationalDataRow> relRowClass = (Class<? extends IRelationalDataRow>) clazz;
        if (header.getAvailableLanguages().length > 1) {
            return new RelationalMultiSheet<>(this, relHeader, RelationalMultiRow.class, relRowClass);
        } else {
            return new RelationalDataSheet<>(this, relHeader, header.getAvailableLanguages()[0], relRowClass);
        }
    }

    public IRelationalSheet<?> getSheet(int id) {
        return (IRelationalSheet<?>) super.getSheet(id);
    }

    public IRelationalSheet<?> getSheet(String name) {
        return (IRelationalSheet<?>) super.getSheet(name);
    }

    public IRelationalRow findReference(int key) {
        if (key <= 0) {
            return null;
        }

        List<SheetDefinition> definitions = definition.getSheetDefinitions()
                .stream()//
                .filter(SheetDefinition::isGenericReferenceTarget)//
                .collect(Collectors.toList());//

        for (SheetDefinition sheetDef : definitions) {
            IRelationalSheet<?> sheet = getSheet(sheetDef.getName());

            if (Arrays.stream(sheet.getHeader().getPages()).noneMatch(it -> it.contains(key))) {
                continue;
            }

            if (!sheet.containsRow(key)) {
                continue;
            }

            return sheet.get(key);
        }

        return null;
    }
}
