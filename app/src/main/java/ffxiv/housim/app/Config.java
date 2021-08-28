package ffxiv.housim.app;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

@Slf4j
public enum Config {
    INSTANCE;

    private final static String GAME_DIR = "GAME_DIR";
    private final static String WORKSPACE = "WORKSPACE";

    private Preferences prefs;

    private String gameDir;

    private String workspace;

    Config() {
        prefs = Preferences.userNodeForPackage(Config.class);
        gameDir = prefs.get(GAME_DIR, "");
        workspace = prefs.get(WORKSPACE, Constants.SAVE_DIR);
    }

    public String getGameDir() {
        return gameDir;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = set(GAME_DIR, gameDir);
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = set(WORKSPACE, workspace);
    }

    private String set(String key, String value) {
        if (value == null) {
            value = "";
        } else {
            value = value.trim();
        }
        prefs.put(key, value);
        return value;
    }

    public void validate() throws FileNotFoundException {
        String gameDir = getGameDir();
        File gameDirectory = new File(getGameDir());
        if (!gameDirectory.exists()) {
            log.warn("FFXIV game directory not exist: {}", gameDirectory);
            throw new FileNotFoundException(gameDir);
        }

        File sqpack = Paths.get(gameDirectory.getPath(), "game", "sqpack").toFile();
        if (!sqpack.exists()) {
            log.error("FFXIV directory [~/game/sqpack] not exist.");
            throw new FileNotFoundException("FFXIV directory [~/game/sqpack] not exist.");
        }

        File ffxivGameVer = Paths.get(gameDirectory.getPath(), "game", "ffxivgame.ver").toFile();
        if (!ffxivGameVer.exists()) {
            log.error("FFXIV file [~/game/ffxivgame.ver] not exist.");
            throw new FileNotFoundException("FFXIV file [~/game/ffxivgame.ver] not exist.");
        }
    }
}
