package ffxiv.housim.saintcoinach.graphics.material;

import lombok.Getter;
import org.checkerframework.checker.regex.qual.Regex;

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
    private boolean containsVariant;

    public PathExpander(String pattern, String replacementFormat, String replacementStainFormat, boolean containsVariant) {
        this.pattern = Pattern.compile(pattern);
        this.replacement = replacementFormat;
        this.stainReplacement = replacementStainFormat;
        this.containsVariant = containsVariant;
    }

    public boolean tryExpand(String input) {
        String path;
        String stainedPath;
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            path = null;
            stainedPath = null;
            return false;
        }
        path = matcher.replaceAll(replacement);
        if (stainReplacement == null || stainReplacement.isBlank()) {
            stainedPath = null;
        } else {
            stainedPath = matcher.replaceAll(stainReplacement);
        }
        return true;
    }
}
