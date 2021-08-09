package ffxiv.housim.saintcoinach.ex.relational.definition;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationDefinition {
    private boolean isCompiled = false;
    @Getter
    private final List<SheetDefinition> sheetDefinitions = new ArrayList<>();
    private final Map<String, SheetDefinition> sheetMap = new HashMap<>();
    private final Map<String, SheetDefinition> sheetDefinitionMap = new HashMap<>();

    @Getter
    @Setter
    private String version;

    public void compile() {
        sheetDefinitions.forEach(it -> {
            sheetMap.put(it.getName(), it);
            it.compile();
        });
        isCompiled = true;
    }

    public SheetDefinition tryGetSheet(String name) {
        if (isCompiled) {
            return sheetMap.get(name);
        }
        return sheetDefinitionMap.get(name);
    }

    public SheetDefinition getOrCreateSheet(String name) {
        SheetDefinition def = tryGetSheet(name);
        if (def == null) {
            def = new SheetDefinition();
            def.setName(name);
            sheetDefinitions.add(def);
            sheetDefinitionMap.put(name, def);
        }
        return def;
    }
}
