package ffxiv.housim.saintcoinach.material;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Found referenced in random models (no idea which ones), and still have to be located
/lambert1.mtrl
/Lambert139.mtrl
/Lambert140.mtrl
/Lambert141.mtrl
/Lambert148.mtrl
/motion_Black.mtrl
/motion_Black5.mtrl
/motion_gray5.mtrl
/motion_red.mtrl
/motion_Skin.mtrl
/motion_Skin5.mtrl
/motion_White.mtrl
/motion_White5.mtrl
/Scene_Material.mtrl
/Scene_Material0.mtrl
/Scene_Material11.mtrl
/Scene_Material12.mtrl
*/
public class PathExpander {
    @Getter
    private Pattern pattern;
    private String replacement;
    private String stainReplacement;
    @Getter
    private boolean containsVariant;

    public PathExpander(String pattern, String replacementFormat, String replacementStainFormat, boolean containsVariant) {
        this.pattern = Pattern.compile(pattern);
        this.replacement = replacementFormat;
        this.stainReplacement = replacementStainFormat;
        this.containsVariant = containsVariant;
    }

    public boolean tryExpand(String input, ExpandResult save) {
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return false;
        }

        save.pathFormat = matcher.replaceAll(replacement);
        save.variantsAvailable = containsVariant;
        if (stainReplacement == null || stainReplacement.isBlank()) {
            save.stainedPathFormat = null;
        } else {
            save.stainedPathFormat = matcher.replaceAll(stainReplacement);
        }
        return true;
    }
}
