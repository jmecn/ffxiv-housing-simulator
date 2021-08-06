package ffxiv.housim.saintcoinach.ex;

import java.util.HashMap;
import java.util.Map;

public enum Language {
    None(0, ""),
    Japanese(1, "ja"),
    English(2, "en"),
    German(3, "de"),
    French(4, "fr"),
    ChineseSimplified(5, "chs"),
    ChineseTraditional(6, "cht"),
    Korean(7, "ko"),
    Unsupported(8, "?");

    private int value;
    private String code;

    private Language(int value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getSuffix() {
        if (code.length() > 0) {
            return "_" + code;
        }

        return "";
    }

    private static Map<Integer, Language> ValueCache = new HashMap<>();
    private static Map<String, Language> CodeCache = new HashMap<>();

    static  {
        for (Language e : values()) {
            ValueCache.put(e.value, e);
            CodeCache.put(e.code, e);
        }
    }

    public static Language of(Integer value) {
        Language lang = ValueCache.get(value);
        if (lang == null) {
            return Language.Unsupported;
        }
        return lang;
    }

    public static Language of(String code) {
        Language lang = CodeCache.get(code);
        if (lang == null) {
            return Language.Unsupported;
        }
        return lang;
    }
}
