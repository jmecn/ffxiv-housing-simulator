package ffxiv.housim.saintcoinach.db.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;

import java.lang.reflect.Type;

public class RepeatDataDefinition implements IDataDefinition {

    private int namingOffset;
    private int repeatCount;
    private IDataDefinition repeatedDefinition;

    public RepeatDataDefinition() {
        namingOffset = 0;
    }

    @Override
    public int getLength() {
        return repeatedDefinition == null ? 0 : repeatCount * repeatedDefinition.getLength();
    }

    @Override
    public Object convert(IDataRow row, Object value, int index) {
        if (index < 0 || index >= getLength()) {
            throw new IndexOutOfBoundsException();
        }

        int innerIndex = index % repeatedDefinition.getLength();

        return repeatedDefinition.convert(row, value, innerIndex);
    }

    @Override
    public String getName(int index) {
        if (index < 0 || index >= getLength()) {
            throw new IndexOutOfBoundsException();
        }

        int repeatNr = index / repeatedDefinition.getLength();
        int innerIndex = index % repeatedDefinition.getLength();

        String baseName = repeatedDefinition.getName(innerIndex);

        return String.format("%s[%d]", baseName, repeatNr + namingOffset);
    }

    @Override
    public String getValueTypeName(int index) {
        if (index < 0 || index >= getLength()) {
            throw new IndexOutOfBoundsException();
        }

        int innerIndex = index % repeatedDefinition.getLength();

        return repeatedDefinition.getValueTypeName(innerIndex);
    }

    @Override
    public Type getValueType(int index) {
        if (index < 0 || index >= getLength()) {
            throw new IndexOutOfBoundsException();
        }

        int innerIndex = index % repeatedDefinition.getLength();

        return repeatedDefinition.getValueType(innerIndex);
    }

    @Override
    public IDataDefinition clone() {
        RepeatDataDefinition def = new RepeatDataDefinition();
        def.namingOffset = namingOffset;
        def.repeatCount = repeatCount;
        def.repeatedDefinition = repeatedDefinition.clone();
        return def;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "repeat");
        obj.addProperty("count", repeatCount);
        obj.add("definition", repeatedDefinition.toJson());
        return obj;
    }

    public static RepeatDataDefinition fromJson(JsonObject obj) {
        RepeatDataDefinition def = new RepeatDataDefinition();
        def.repeatCount = obj.get("count").getAsInt();
        def.repeatedDefinition = DataDefinitionSerializer.fromJson(obj.getAsJsonObject("definition"));
        return def;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
        repeatedDefinition.resolveReferences(sheetDef);
    }
}
