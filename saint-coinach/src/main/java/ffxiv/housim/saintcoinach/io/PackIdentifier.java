package ffxiv.housim.saintcoinach.io;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PackIdentifier {

    private final static String DefaultExpansion = "ffxiv";

    private final static Map<String, Integer> TypeToKeyMap;

    private final static Map<Integer, String> KeyToTypeMap;

    private final static Map<String, Integer> ExpansionToKeyMap;

    private final static Map<Integer, String> KeyToExpansionMap;

    static {
        TypeToKeyMap = new HashMap<>();
        TypeToKeyMap.put("common", 0x00);
        TypeToKeyMap.put("bgcommon", 0x01);
        TypeToKeyMap.put("bg", 0x02);
        TypeToKeyMap.put("cut", 0x03);
        TypeToKeyMap.put("chara", 0x04);
        TypeToKeyMap.put("shader", 0x05);
        TypeToKeyMap.put("ui", 0x06);
        TypeToKeyMap.put("sound", 0x07);
        TypeToKeyMap.put("vfx", 0x08);
        TypeToKeyMap.put("ui_script", 0x09);
        TypeToKeyMap.put("exd", 0x0a);
        TypeToKeyMap.put("game_script", 0x0b);
        TypeToKeyMap.put("music", 0x0c);
        TypeToKeyMap.put("_sqpack_test", 0x12);
        TypeToKeyMap.put("_debug", 0x13);

        KeyToTypeMap = new HashMap<>();
        TypeToKeyMap.forEach((k, v) -> KeyToTypeMap.put(v, k));

        ExpansionToKeyMap = new HashMap<>();
        ExpansionToKeyMap.put("ffxiv", 0x00);
        ExpansionToKeyMap.put("ex1", 0x01);
        ExpansionToKeyMap.put("ex2", 0x02);
        ExpansionToKeyMap.put("ex3", 0x03);

        KeyToExpansionMap = new HashMap<>();
        ExpansionToKeyMap.forEach((k, v) -> KeyToExpansionMap.put(v, k));

    }

    @Getter
    private String type;

    @Getter
    private int typeKey;

    @Getter
    private String expansion;

    @Getter
    private int expansionKey;

    @Getter
    private int number;

    private PackIdentifier() {
    }

    public PackIdentifier(int type, int expansion, int number) {
        this.typeKey = type;
        this.expansionKey = expansion;
        this.number = number;

        this.type = KeyToTypeMap.get(type);
        this.expansion = KeyToExpansionMap.get(expansion);
    }

    public PackIdentifier(String type, String expansion, int number) {
        this.type = type;
        this.expansion = expansion;
        this.number = number;

        this.typeKey = TypeToKeyMap.get(type);
        this.expansionKey = ExpansionToKeyMap.get(expansion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackIdentifier that = (PackIdentifier) o;
        return typeKey == that.typeKey && expansionKey == that.expansionKey && number == that.number;
    }

    @Override
    public int hashCode() {
        int hash = (typeKey << 16 | expansionKey << 8 | number);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%02x%02x%02x", typeKey, expansionKey, number);
    }

    public static PackIdentifier tryGet(@NonNull String fullPath) {
        PackIdentifier value;

        int typeSep = fullPath.indexOf('/');
        if (typeSep <= 0) {
            return null;
        }
        String type = fullPath.substring(0, typeSep);
        if (!TypeToKeyMap.containsKey(type)) {
            return null;
        }

        int expSep = fullPath.indexOf('/', typeSep + 1);

        String expansion = null;
        int number = 0;
        if (expSep > typeSep) {
            expansion = fullPath.substring(typeSep + 1, expSep);
            int numberEnd = fullPath.indexOf('_', expSep);
            if (numberEnd - expSep == 3) {
                String numStr = fullPath.substring(expSep + 1, numberEnd);
                try {
                    number = Integer.parseInt(numStr);
                } catch (Exception e) {
                    log.error("Parse int failed: {}", numStr, e);
                }
            }
        }

        if (expansion == null || !ExpansionToKeyMap.containsKey(expansion)) {
            expansion = DefaultExpansion;
        }

        value = new PackIdentifier(type, expansion, number);
        log.info("{} <-- {}", value, fullPath);
        return value;
    }

    public static Integer tryGetSqPackKey(@NonNull String path) {
        int idx = path.indexOf("/");
        if (idx > 0) {
            String prefix = path.substring(0, idx);
            return TypeToKeyMap.get(prefix);
        }

        return null;
    }

    public static String tryGetSqPackName(@NonNull Integer key) {
        return KeyToTypeMap.get(key);
    }

}
