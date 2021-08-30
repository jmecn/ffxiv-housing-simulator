package ffxiv.housim.saintcoinach.scene.terrain;

import ffxiv.housim.saintcoinach.db.xiv.entity.level.TerritoryType;
import ffxiv.housim.saintcoinach.io.Pack;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.scene.lgb.LgbFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Territory {
    PackCollection packs;
    @Getter
    private Terrain terrain;
    private String basePath;
    @Getter
    private String name;

    @Getter
    private List<LgbFile> lgbFiles;

    private LgbFile bg;
    private LgbFile planmap;
    private LgbFile planevent;

    /**
     *
     * @param type
     */
    public Territory(TerritoryType type) {
        this(type.getSheet().getCollection().getPackCollection(), type.getName(), type.getBg());
    }

    /**
     *
     * @param packs
     * @param name
     * @param levelPath not including <code>bg/</code>
     */
    public Territory(PackCollection packs, String name, String levelPath) {
        this.packs = packs;
        this.name = name;
        int i = levelPath.indexOf("/level/");
        this.basePath = "bg/" + levelPath.substring(0, i + 1);

        build();
    }

    private void build() {
        String terrainPath = basePath + "bgplate/terrain.tera";

        PackFile file = packs.tryGetFile(terrainPath);
        if (file != null) {
            terrain = new Terrain(file);
        }

        lgbFiles = new ArrayList<>(3);

        bg = tryGetLgb("level/bg.lgb");
        planmap = tryGetLgb("level/planmap.lgb");
        planevent = tryGetLgb("level/planevent.lgb");

        if (bg != null) {
            lgbFiles.add(bg);
        }
        if (planmap!= null) {
            lgbFiles.add(planmap);
        }
        if (planevent != null) {
            lgbFiles.add(planevent);
        }
    }

    private LgbFile tryGetLgb(String name) {
        String path = basePath + name;
        PackFile file = packs.tryGetFile(path);
        return file == null ? null : new LgbFile(file);
    }
}
