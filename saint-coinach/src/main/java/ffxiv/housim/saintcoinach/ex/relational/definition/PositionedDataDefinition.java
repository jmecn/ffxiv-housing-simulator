package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

public class PositionedDataDefinition implements Cloneable {
    @Getter
    @Setter
    public IDataDefinition innerDefinition;
    @Getter
    @Setter
    private int index;

    public int getLength() {
        return innerDefinition == null ? 0 : innerDefinition.getLength();
    }

    public PositionedDataDefinition clone() {
        PositionedDataDefinition clone = new PositionedDataDefinition();
        clone.index = index;
        clone.innerDefinition = innerDefinition.clone();
        return clone;
    }

    public Object convert(IDataRow row, Object value,int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.convert(row, value, innerIndex);
    }

    public String getName(int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.getName(innerIndex);
    }

    public String getValueTypeName(int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.getValueTypeName(innerIndex);
    }

    public Type getValueType(int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.getValueType(innerIndex);
    }

    public JsonObject toJson() {
        JsonObject obj = innerDefinition.toJson();
        if (index > 0) {
            obj.addProperty("index", index);
        }
        return obj;
    }

    public static PositionedDataDefinition fromJson(JsonObject obj) {
        PositionedDataDefinition def = new PositionedDataDefinition();

        if (obj.has("index")) {
            def.index = obj.get("index").getAsInt();
        } else {
            def.index = 0;
        }

        def.innerDefinition = DataDefinitionSerializer.fromJson(obj);

        return def;
    }

    public void resolveReferences(SheetDefinition sheetDef) {
        innerDefinition.resolveReferences(sheetDef);
    }
}
