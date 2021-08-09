package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

public class PositionedDataDefintion implements Cloneable {
    @Getter
    @Setter
    public IDataDefinition innerDefinition;
    @Getter
    @Setter
    private int index;

    public int getLength() {
        return innerDefinition == null ? 0 : innerDefinition.getLength();
    }

    public PositionedDataDefintion clone() {
        PositionedDataDefintion clone = new PositionedDataDefintion();
        clone.index = index;
        clone.innerDefinition = innerDefinition.clone();
        return clone;
    }

    public Object convert(IDataRow row, Object value,int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.convert(row, value, index);
    }

    public String getName(int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.getName(index);
    }

    public String getValueTypeName(int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.getValueTypeName(index);
    }

    public Type getValueType(int index) {
        int innerIndex = index - getIndex();
        if (innerIndex < 0 || innerIndex >= getLength()) {
            throw new IndexOutOfBoundsException("Index out of range " + index);
        }

        return innerDefinition.getValueType(index);
    }

    public JsonObject toJson() {
        JsonObject obj = innerDefinition.toJson();
        if (index > 0) {
            obj.addProperty("index", index);
        }
        return obj;
    }

    public static PositionedDataDefintion fromJson(JsonObject obj) {
        PositionedDataDefintion def = new PositionedDataDefintion();

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
