package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupDataDefinition implements IDataDefinition {

    private List<IDataDefinition> members = new ArrayList<>();
    private int length;

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Object convert(IDataRow row, Object value, int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }

        Object convertedValue = value;
        int pos = 0;
        for (IDataDefinition member : members) {
            int newPos = pos + member.getLength();
            if (newPos > index) {
                int innerIndex = index - pos;
                convertedValue = member.convert(row, value, innerIndex);
                break;
            }
            pos = newPos;
        }
        return convertedValue;
    }

    @Override
    public String getName(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }

        String value = null;
        int pos = 0;
        for (IDataDefinition member : members) {
            int newPos = pos + member.getLength();
            if (newPos > index) {
                int innerIndex = index - pos;
                value = member.getName(innerIndex);
                break;
            }
            pos = newPos;
        }
        return value;
    }

    @Override
    public String getValueTypeName(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }

        String value = null;
        int pos = 0;
        for (IDataDefinition member : members) {
            int newPos = pos + member.getLength();
            if (newPos > index) {
                int innerIndex = index - pos;
                value = member.getValueTypeName(innerIndex);
                break;
            }
            pos = newPos;
        }
        return value;
    }

    @Override
    public Type getValueType(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }

        Type value = null;
        int pos = 0;
        for (IDataDefinition member : members) {
            int newPos = pos + member.getLength();
            if (newPos > index) {
                int innerIndex = index - pos;
                value = member.getValueType(innerIndex);
                break;
            }
            pos = newPos;
        }
        return value;
    }

    @Override
    public IDataDefinition clone() {
        GroupDataDefinition def = new GroupDataDefinition();
        int length = 0;
        for (IDataDefinition member : members) {
            length += member.getLength();
            def.members.add(member.clone());
        }
        def.length = length;
        return def;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "group");

        JsonArray array = new JsonArray();
        for (IDataDefinition member : members) {
            array.add(member.toJson());
        }
        obj.add("members", array);
        return obj;
    }

    public static GroupDataDefinition fromJson(JsonObject obj) {
        GroupDataDefinition def = new GroupDataDefinition();

        JsonArray array = obj.getAsJsonArray("members");
        int size = array.size();
        int length = 0;
        for (int i = 0; i < size; i++) {
            IDataDefinition member = DataDefinitionSerializer.fromJson((JsonObject) array.get(i));
            length += member.getLength();
            def.members.add(member);
        }
        def.length = length;

        return def;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {
        for (IDataDefinition member : members) {
            member.resolveReferences(sheetDef);
        }
    }
}
