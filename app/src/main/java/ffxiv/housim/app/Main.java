package ffxiv.housim.app;

import com.google.common.io.Resources;
import com.jme3.system.AppSettings;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.BackingStoreException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        AppSettings settings = getSettings(args);

        if (showSettings(settings)) {
            startGame(settings);
        }
    }

    private static AppSettings getSettings(String[] args) {
        AppSettings setting = new AppSettings(true);

        setting.setTitle(Constants.TITLE);
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        setting.setGammaCorrection(true);
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);

        // read from registry
        try {
            setting.load(Constants.TITLE);
        } catch (BackingStoreException ex) {
            log.warn("Failed to load settings", ex);
        }

        return setting;
    }

    private static boolean showSettings(AppSettings settings) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Cannot run from EDT");
        }
        if (GraphicsEnvironment.isHeadless()) {
            throw new IllegalStateException("Cannot show dialog in headless environment");
        }

        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            // doesn't matter
        }

        final AtomicBoolean done = new AtomicBoolean();
        final AtomicInteger result = new AtomicInteger();
        final Object lock = new Object();

        final SettingsDialog.SelectionListener selectionListener = selection -> {
            synchronized (lock) {
                done.set(true);
                result.set(selection);
                lock.notifyAll();
            }
        };

        URL image = Resources.getResource("ffxiv/housim/app/Monkey.png");
        SwingUtilities.invokeLater(() -> {
            synchronized (lock) {
                SettingsDialog dialog = new SettingsDialog(settings, image, false);
                dialog.setSelectionListener(selectionListener);
                dialog.showDialog();
            }
        });

        synchronized (lock) {
            while (!done.get()) {
                try {
                    lock.wait();
                } catch (InterruptedException ex) {
                }
            }
        }

        return (result.get() == SettingsDialog.APPROVE_SELECTION);
    }

    private static void startGame(AppSettings settings) {
        String gameDir = settings.getString(Constants.GAME_DIR);

        // init game dir
        ARealmReversed ffxiv;
        try {
            ffxiv = new ARealmReversed(gameDir, Language.ChineseSimplified);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Initialize ffxiv failed", e);
            JOptionPane.showMessageDialog(null, "模拟器启动失败!");
            return;
        }

        // start game
        HousingSimulator app = new HousingSimulator(ffxiv);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    public static boolean checkGameDir(String gameDir) {
        if (gameDir == null || gameDir.isEmpty()) {
            return false;
        }

        Path home = Paths.get(gameDir);
        Path sqpack = Paths.get(gameDir, "game", "sqpack");
        Path gameVer = Paths.get(gameDir, "game", "ffxivgame.ver");

        return Files.exists(home) && Files.isDirectory(home)//
                && Files.exists(sqpack)  && Files.isDirectory(sqpack) //
                && Files.exists(gameVer) && Files.isRegularFile(gameVer) ;
    }
}
