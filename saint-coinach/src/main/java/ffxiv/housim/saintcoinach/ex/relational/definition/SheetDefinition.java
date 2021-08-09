package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.*;

public class SheetDefinition {

    private Map<Integer, PositionedDataDefintion> columnDefinitionMap;
    private Map<Integer, String> columnIndexToNameMap;
    private Map<String, Integer> columnNameToIndexMap;
    private Map<Integer, String> columnValueTypeNames;
    private Map<Integer, Type> columnValueTypes;
    @Getter
    private List<PositionedDataDefintion> dataDefinitions = new ArrayList<>();
    private Integer defaultColumnIndex;
    private boolean isCompiled = false;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String defaultColumn;
    @Getter
    @Setter
    private boolean isGenericReferenceTarget;

    public SheetDefinition() {
    }

    public void compile() {
        columnDefinitionMap = new HashMap<>();
        columnNameToIndexMap = new HashMap<>();
        columnIndexToNameMap = new HashMap<>();
        columnValueTypeNames = new HashMap<>();
        columnValueTypes = new HashMap<>();

        dataDefinitions.sort(Comparator.comparingInt(PositionedDataDefintion::getIndex));

        for (PositionedDataDefintion def : dataDefinitions) {
            for (int i = 0; i < def.getLength(); ++i) {
                int offset = def.getIndex() + i;
                columnDefinitionMap.put(offset, def);

                String name = def.getName(offset);
                columnNameToIndexMap.put(name, offset);
                columnIndexToNameMap.put(offset, name);
                columnValueTypeNames.put(offset, def.getValueTypeName(offset));
                columnValueTypes.put(offset, def.getValueType(offset));
            }
        }

        if (defaultColumn != null && defaultColumn.trim().length() > 0) {
            defaultColumnIndex = columnNameToIndexMap.get(defaultColumn);
        } else {
            defaultColumnIndex = null;
        }

        isCompiled = true;
    }

    public PositionedDataDefintion tryGetDefinition(int index) {
        if (isCompiled) {
            return columnDefinitionMap.get(index);
        }

        return dataDefinitions.stream()//
                .filter(it -> it.getIndex() <= index && index < (it.getIndex() + it.getLength()))//
                .findFirst()//
                .orElse(null);
    }

    public Integer getDefaultColumnIndex() {
        if (isCompiled) {
            return defaultColumnIndex;
        }

        if (defaultColumn != null && defaultColumn.trim().length() > 0) {
            return findColumn(defaultColumn);
        } else {
            return null;
        }
    }

    public Integer findColumn(@NonNull String columnName) {
        if (isCompiled) {
            return columnNameToIndexMap.get(columnName);
        }

        for (PositionedDataDefintion def : dataDefinitions) {
            for (int i = 0; i < def.getLength(); i++) {
                String n = def.getName(def.getIndex() + i);
                if (columnName.equals(n)) {
                    return def.getIndex() + i;
                }
            }
        }

        return null;
    }

    public Collection<String> getAllColumnNames() {
        if (isCompiled) {
            return columnNameToIndexMap.keySet();
        }

        List<String> names = new ArrayList<>();
        for (PositionedDataDefintion def : dataDefinitions) {
            for (int i = 0; i < def.getLength(); i++) {
                String n = def.getName(def.getIndex() + i);
                names.add(n);
            }
        }

        return names;
    }


    public String getColumnName(int index) {
        if (isCompiled) {
            return columnIndexToNameMap.get(index);
        }

        PositionedDataDefintion def = tryGetDefinition(index);
        return def != null ? def.getName(index) : null;
    }

    public String getValueTypeName(int index) {
        if (isCompiled) {
            return columnValueTypeNames.get(index);
        }

        PositionedDataDefintion def = tryGetDefinition(index);
        return def != null ? def.getValueTypeName(index) : null;
    }

    public Type getValueType(int index) {
        if (isCompiled) {
            return columnValueTypes.get(index);
        }

        PositionedDataDefintion def = tryGetDefinition(index);
        return def != null ? def.getValueType(index) : null;
    }

    public Object convert(IDataRow row, Object value, int index) {
        PositionedDataDefintion def = tryGetDefinition(index);
        return def != null ? def.convert(row, value, index) : value;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("sheet", name);
        if (defaultColumn != null) {
            obj.addProperty("defaultColumn", defaultColumn);
        }
        if (isGenericReferenceTarget) {
            obj.addProperty("isGenericReferenceTarget", true);
        }

        JsonArray definitions = new JsonArray();
        for (PositionedDataDefintion def : dataDefinitions) {
            definitions.add(def.toJson());
        }
        obj.add("definitions", definitions);
        return obj;
    }

    public static SheetDefinition fromJson(JsonObject obj) {
        SheetDefinition sheetDef = new SheetDefinition();

        sheetDef.name = obj.get("sheet").getAsString();
        sheetDef.defaultColumn = obj.get("defaultColumn").getAsString();
        sheetDef.isGenericReferenceTarget = obj.has("isGenericReferenceTarget");

        JsonArray definitions = obj.getAsJsonArray("definitions");

        for (JsonElement ele : definitions) {
            if (ele instanceof JsonObject) {
                JsonObject json = (JsonObject) ele;
                sheetDef.dataDefinitions.add(PositionedDataDefintion.fromJson(json));
            }
        }

        for (PositionedDataDefintion dataDef : sheetDef.dataDefinitions) {
            dataDef.resolveReferences(sheetDef);
        }

        return sheetDef;
    }

    @Override
    public String toString() {
        return name;
    }
}
