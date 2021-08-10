package ffxiv.housim.saintcoinach;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.relational.definition.RelationDefinition;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.xiv.XivCollection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ARealmReversed {
    /**
     * Default file name of the archive containing current and past data mappings.
     */
    private final static String DefaultStateFile = "SaintCoinach.History.zip";

    /**
     * File name containing the current version string.
     */
    private final static String VersionFile = "ffxivgame.ver";

    /**
     * Format string to create the file name for update reports in text form. {0} is the previous and {1} the new version.
     */
    private final static String UpdateReportTextFile = "logs/report-{0}-{1}.log";

    /**
     * Format string to create the file name for update reports in JSON form. {0} is the previous and {1} the new version.
     */
    private final static String UpdateReportJsonFile = "logs/report-{0}-{1}.json";

    /**
     * Format string to create the file name for update reports in binary form. {0} is the previous and {1} the new version.
     */
    private final static String UpdateReportBinFile = "logs/report-{0}-{1}.bin";

    /**
     * Encoding to use inside the ZipFile.
     */
    private final static Charset ZipEncoding = StandardCharsets.UTF_8;

    /**
     * Game data collection for the data files.
     */
    private XivCollection gameData;

    /**
     * Root directory of the game installation.
     */
    private File gameDirectory;

    /**
     * Version of the game data.
     */
    private String gameVersion;

    /**
     * Pack collection for the data files.
     */
    private PackCollection packs;

    /**
     * Archive file containing current and past data mappings.
     */
    private File stateFile;

    /**
     * Initializes a new instance of the ARealmReversed class.
     *
     * @param gamePath Directory path to the game installation.
     * @param language Initial language to use.
     */
    public ARealmReversed(String gamePath, Language language) throws IOException {
        this(new File(gamePath), new File(DefaultStateFile), language, null);
    }

    /**
     * Initializes a new instance of the ARealmReversed class.
     *
     * @param gamePath Directory path to the game installation.
     * @param storePath Path to the file used for storing definitions and history.
     * @param language Initial language to use.
     */
    public ARealmReversed(String gamePath, String storePath, Language language) throws IOException {
        this(new File(gamePath), new File(storePath), language, null);
    }

    /**
     * Initializes a new instance of the ARealmReversed class.
     *
     * @param gamePath Directory path to the game installation.
     * @param storePath Path to the file used for storing definitions and history.
     * @param language Initial language to use.
     * @param libraPath Path to the Libra Eorzea database file.
     */
    public ARealmReversed(String gamePath, String storePath, Language language, String libraPath) throws IOException {
        this(new File(gamePath), new File(storePath), language, new File(libraPath));
    }

    /**
     * Initializes a new instance of the ARealmReversed class.
     *
     * @param gameDirectory Directory of the game installation.
     * @param storeFile File used for storing definitions and history.
     * @param language Initial language to use.
     * @param libraFile Location of the Libra Eorzea database file, or <code>null</code> if it should not be used.
     */
    public ARealmReversed(File gameDirectory, File storeFile, Language language, File libraFile) throws IOException {
        if (!gameDirectory.exists()) {
            log.warn("FFXIV game directory not exist: {}", gameDirectory);
            throw new FileNotFoundException(gameDirectory.getPath());
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

        this.gameDirectory = gameDirectory;
        packs = new PackCollection(sqpack.getPath());
        gameData = new XivCollection(packs, libraFile);
        gameData.setActiveLanguage(language);

        gameVersion = Files.asCharSource(ffxivGameVer, StandardCharsets.UTF_8).read();
        stateFile = storeFile;

        gameData.setDefinition(readDefinition());
        gameData.getDefinition().compile();

    }

    private RelationDefinition readDefinition() throws IOException {
        File folder = new File("Definitions");

        File versionFile = Paths.get("Definitions", "game.ver").toFile();
        if (!versionFile.exists()) {
            log.error("Definitions/game.ver not exist.");
            throw new FileNotFoundException("Definitions/game.ver must exist.");
        }

        String version = Files.asCharSource(versionFile, StandardCharsets.UTF_8).read();

        RelationDefinition def = new RelationDefinition();
        def.setVersion(version);

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        JsonParser parser = new JsonParser();
        for (File sheetFileName : files) {
            String json = Files.asCharSource(sheetFileName, StandardCharsets.UTF_8).read();
            JsonElement root = parser.parse(json);

            SheetDefinition sheetDef = SheetDefinition.fromJson((JsonObject) root);
            def.getSheetDefinitions().add(sheetDef);

            if (!gameData.sheetExists(sheetDef.getName())) {
                log.warn("Defined sheet {} is missing.");
            }
        }

        return def;
    }
}
