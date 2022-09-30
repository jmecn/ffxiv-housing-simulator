package ffxiv.housim.saintcoinach.db.ex.relational.valueconverters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.db.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalSheet;
import ffxiv.housim.saintcoinach.db.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.db.ex.relational.RelationalExCollection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Arrays;

@Slf4j
public class MultiReferenceConverter implements IValueConverter<IRelationalRow> {

    @Getter
    private String[] targets;

    @Override
    public String getTargetTypeName() {
        return "Row";
    }

    @Override
    public Type getTargetType() {
        return IRelationalRow.class;
    }

    @Override
    public IRelationalRow convert(IDataRow row, Object rawValue) {
        if (targets == null) {
            return null;
        }

        int key;

        if (rawValue instanceof Integer) {
            key = (int) rawValue;
        } else if (rawValue instanceof Short) {
            key = 0xFFFF & (short) rawValue;
        } else if (rawValue instanceof Byte) {
            key = 0xFF & (byte) rawValue;
        } else if (rawValue instanceof Number){
            Number val = (Number) rawValue;
            key = val.intValue();
        } else {
            log.warn("Unsupported class cast to Integer: {}", rawValue.getClass());
            return null;
        }

        RelationalExCollection coll = (RelationalExCollection) row.getSheet().getCollection();

        for (String target : targets) {
            IRelationalSheet<?> sheet = coll.getSheet(target);
            if (Arrays.stream(sheet.getHeader().getPages()).noneMatch(it -> it.contains(key))) {
                continue;
            }

            if (sheet.containsRow(key)) {
                return sheet.get(key);
            }
        }

        return null;
    }

    @Override
    public JsonObject toJson() {
        JsonArray array = new JsonArray();
        for (String target :  targets) {
            array.add(target);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "multiref");
        obj.add("targets", array);
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {

    }

    public static MultiReferenceConverter fromJson(JsonObject obj) {
        JsonArray array = obj.getAsJsonArray("targets");

        int size = array.size();
        String[] targets = new String[size];
        for (int i = 0; i < size; i++) {
            targets[i] = array.get(i).getAsString();
        }

        MultiReferenceConverter converter = new MultiReferenceConverter();
        converter.targets = targets;

        return converter;
    }

}
