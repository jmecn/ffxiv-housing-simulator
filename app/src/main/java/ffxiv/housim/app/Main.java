package ffxiv.housim.app;

import com.google.common.io.Resources;
import com.jme3.system.AppSettings;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.BackingStoreException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        String os = System.getProperty("os.name");
        boolean isMacOS = os.toLowerCase().contains("mac");
        AppSettings settings = getSettings(args);

        if (isMacOS) {
            String gameDir = settings.getString(Constants.GAME_DIR);
            if (checkGameDir(gameDir)) {
                startGame(settings);
            } else {
                showSettings(settings);
            }
        } else {
             if (showSettings(settings)) {
                 startGame(settings);
             }
        }
    }

    private static BufferedImage[] getIcons() {
        BufferedImage[] images = new BufferedImage[4];
        InputStream is = Main.class.getResourceAsStream("/icons/icon_128x128.png");
        try {
            BufferedImage image = ImageIO.read(is);
            images[0] = image;
            images[1] = scale(image,64);
            images[2] = scale(image,32);
            images[3] = scale(image,16);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("read icon failed", e);
        }

        return images;
    }

    private static BufferedImage scale(BufferedImage image, int size) {
        Image img = image.getScaledInstance(size, size, 0);
        BufferedImage to = new BufferedImage(size, size, image.getType());
        to.getGraphics().drawImage(img, 0, 0, null);
        return to;
    }

    private static AppSettings getSettings(String[] args) {
        AppSettings setting = new AppSettings(true);

        setting.setTitle(Constants.TITLE);
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        setting.setGammaCorrection(false);
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);
        setting.setUseRetinaFrameBuffer(false);

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

        // set icons
        settings.setIcons(getIcons());

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

        URL image = null;
        // URL image = Resources.getResource("ffxiv/housim/app/Monkey.png");
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
