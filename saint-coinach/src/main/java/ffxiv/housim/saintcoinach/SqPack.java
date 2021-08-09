package ffxiv.housim.saintcoinach;

import ffxiv.housim.saintcoinach.io.Pack;
import lombok.Getter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SqPack {
    private String gameDir;

    @Getter
    private String packDir;

    private List<Pack> packs;

    public SqPack(String gameDir) {
        Path dir = Paths.get(gameDir);
        Path sqpack = Paths.get(gameDir, "game", "sqpack");
        File ffxivGameVer = Paths.get(gameDir, "game", "ffxivgame.ver").toFile();

        packDir = sqpack.toString();
    }
}
