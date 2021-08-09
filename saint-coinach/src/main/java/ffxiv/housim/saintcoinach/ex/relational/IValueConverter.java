package ffxiv.housim.saintcoinach.ex.relational;

import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;

import java.lang.reflect.Type;

public interface IValueConverter {
    String getTargetTypeName();

    Type getTargetType();

    Object convert(IDataRow row, Object rawValue);

    Object toJson();//

    void resolveReferences(SheetDefinition sheetDef);
}
