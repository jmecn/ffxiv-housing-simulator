package ffxiv.housim.app;

import java.util.prefs.Preferences;

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
}
