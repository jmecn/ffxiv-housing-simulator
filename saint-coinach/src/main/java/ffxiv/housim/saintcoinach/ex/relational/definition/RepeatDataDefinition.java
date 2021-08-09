package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;

public class RepeatDataDefinition implements IDataDefinition {

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Object convert(IDataRow row, Object value, int index) {
        return null;
    }

    @Override
    public String getName(int index) {
        return null;
    }

    @Override
    public String getValueTypeName(int index) {
        return null;
    }

    @Override
    public Type getValueType(int index) {
        return null;
    }

    @Override
    public IDataDefinition clone() {
        return null;
    }

    @Override
    public JsonObject toJson() {
        return null;
    }

    public static RepeatDataDefinition fromJson(JsonObject obj) {
        return null;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {

    }
}
