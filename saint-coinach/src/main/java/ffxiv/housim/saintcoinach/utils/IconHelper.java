package ffxiv.housim.saintcoinach.utils;

import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;

public final class IconHelper {

    //final static String IconFileFormat = "ui/icon/%03d000/%s%06d_hr1.tex";// 高清
    final static String IconFileFormat = "ui/icon/%03d000/%s%06d.tex";

    public static ImageFile getIcon(PackCollection pack, int nr) {
        return getIcon(pack, "", nr);
    }

    public static ImageFile getIcon(PackCollection pack, Language language, int nr) {
        String type = language.getCode();
        if (type.length() > 0) {
            type = type + "/";
        }
        return getIcon(pack, type, nr);
    }

    public static ImageFile getIcon(PackCollection pack, String type, int nr) {
        if (type == null) {
            type = "";
        }

        if (type.length() > 0 && !type.endsWith("/")) {
            type = type + "/";
        }

        String filePath = String.format(IconFileFormat, nr / 1000, type, nr);

        PackFile file = pack.tryGetFile(filePath);

        if (file == null && type.length() > 0) {
            // Couldn't get specific type, try for generic version.
            filePath = String.format(IconFileFormat, nr / 1000, "", nr);
            file = pack.tryGetFile(filePath);
            if (file == null) {
                // Couldn't get generic version either, that's a shame.
                file = null;
            }
        }
        return (ImageFile) file;
    }
}
