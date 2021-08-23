package ffxiv.housim.saintcoinach.db.ex.relational;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.definition.SheetDefinition;

import java.lang.reflect.Type;

public interface IValueConverter<T> {
    String getTargetTypeName();

    Type getTargetType();

    T convert(IDataRow row, Object rawValue);

    JsonObject toJson();

    void resolveReferences(SheetDefinition sheetDef);
}
