package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.util.TempVars;
import com.simsilica.es.EntityData;
import ffxiv.housim.app.state.indoor.IndoorState;
import ffxiv.housim.ui.lemur.menubar.*;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
@Slf4j
public class MainMenu extends BaseAppState {

    // Resource bundle for i18n.
    ResourceBundle i18n = ResourceBundle.getBundle("ffxiv.housim.i18n/MainMenu");

    private Node guiNode;

    private Node rootNode;

    private Camera cam;

    private Application app;

    private AppStateManager stateManager;

    private InputManager inputManager;
    private LemurMenuBar menuBar;

    private EntityData ed;

    public MainMenu(EntityData ed) {
        this.ed = ed;
    }

    @Override
    protected void initialize(Application app) {

        this.app = app;
        this.cam = app.getCamera();
        this.stateManager = app.getStateManager();
        this.inputManager = app.getInputManager();

        if (app instanceof SimpleApplication simpleApp) {
            guiNode = simpleApp.getGuiNode();
            rootNode = simpleApp.getRootNode();
        }

        createContextMenu();
        initInput();
    }

    private void initInput() {
        inputManager.addMapping("RIGHT_CLICK", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("LEFT_CLICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {

                if (isPressed) {
                    switch (name) {
                        case "RIGHT_CLICK": {
                            rightClick();
                            break;
                        }
                        case "LEFT_CLICK": {
                            hide();
                            break;
                        }
                    }
                }
            }
        }, "RIGHT_CLICK", "LEFT_CLICK");
    }

    private void hide() {
        // if the context menu is already visible remove any submenus that may be active.
        if (menuBar.getSpatial().getParent() != null) {
            menuBar.hideAllMenus();
            menuBar.getSpatial().removeFromParent();
        }
    }

    private void rightClick() {

        TempVars vars = TempVars.get();

        Vector2f cursor = inputManager.getCursorPosition();
        log.info("cursor:{}", cursor);

        Vector3f dir = cam.getWorldCoordinates(cursor, 1f, vars.vect1);
        dir.subtractLocal(cam.getLocation());
        Ray ray = new Ray(cam.getLocation(), dir);

        CollisionResults results = vars.collisionResults;
        results.clear();

        rootNode.collideWith(ray, results);

        CollisionResult result = results.getClosestCollision();
        if (result != null) {
            log.info("geom:{}", result.getGeometry().getName());
        }

        // if the context menu is already visible remove any submenus that may be active.
        if (menuBar.getSpatial().getParent() == null) {
            guiNode.attachChild(menuBar.getSpatial());
        }
        // set the location to the position that was clicked.
        menuBar.setLocation(cursor.getX(), cursor.getY());

        vars.release();
    }
    private void createContextMenu() {
        final boolean horizontal = false;

        menuBar = new LemurMenuBar(100, horizontal, cam);

        // File Menu
        Menu fileMenu = menuBar.add(new Menu(i18n.getString("file")));

        // File -> New
        Menu newMenu = fileMenu.add(new Menu(i18n.getString("file.new")));

        MenuItem newIndoor = newMenu.add(new MenuItem(i18n.getString("file.new.indoor")));
        newIndoor.addClickCommand(source -> newIndoor());

        MenuItem newOutdoor = newMenu.add(new MenuItem(i18n.getString("file.new.outdoor")));
        newOutdoor.addClickCommand(source -> newOutdoor());
        newOutdoor.setEnabled(false);

        // File -> Open
        MenuItem openMenuItem = fileMenu.add(new MenuItem(i18n.getString("file.open")));
        openMenuItem.addClickCommand(source -> open());

        Menu openRecentMenu = fileMenu.add(new Menu(i18n.getString("file.openRecent")));
        setRecentMenu(openRecentMenu);

        MenuItem save = fileMenu.add(new MenuItem(i18n.getString("file.save")));
        save.addClickCommand(source -> save());
        save.setEnabled(false);

        MenuItem saveAs = fileMenu.add(new MenuItem(i18n.getString("file.saveAs")));
        saveAs.addClickCommand(source -> saveAs());
        saveAs.setEnabled(false);

        // Edit Menu
        Menu editMenu = menuBar.add(new Menu(i18n.getString("cfg")));

        // Edit -> Checkbox
        CheckboxMenuItem checkboxMenuItem = editMenu.add(new CheckboxMenuItem(i18n.getString("cfg.bgm")));

        // Help
        Menu helpMenu = menuBar.add(new Menu(i18n.getString("help")));
        MenuItem aboutMenuItem = helpMenu.add(new MenuItem(i18n.getString("help.about")));

        menuBar.add(new MenuSeparator());

        // File -> Exit
        MenuItem exitMenuItem = menuBar.add(new MenuItem(i18n.getString("file.exit")));
        exitMenuItem.addClickCommand(source -> exit());
    }
    /**
     * load recent blueprint
     *
     * @param recentMenu
     */
    private void setRecentMenu(Menu recentMenu) {
        List<String> recentFiles = new ArrayList<>();
        recentFiles.add("Blueprint-2021-09-01-12-23-32");
        recentFiles.add("Blueprint-2021-09-01-22-37-03");
        if (recentFiles != null && recentFiles.size() > 0) {
            for (String name : recentFiles) {
                MenuItem item = recentMenu.add(new MenuItem(name));
                item.addClickCommand(source -> {
                    // TODO open specified blueprint
                });
            }
        } else {
            MenuItem none = recentMenu.add(new MenuItem("None"));
            none.setEnabled(false);
        }
    }

    @Override
    protected void cleanup(Application app) {
        menuBar.getSpatial().removeFromParent();
        menuBar = null;
    }

    @Override
    protected void onEnable() {
        guiNode.attachChild(menuBar.getSpatial());
    }

    @Override
    protected void onDisable() {
        menuBar.getSpatial().removeFromParent();
    }

    private void newIndoor() {
        hide();
        // TODO
        IndoorState state = stateManager.getState(IndoorState.class);
        if (state == null) {
            state = new IndoorState(ed);
            stateManager.attach(state);
        }

    }

    private void newOutdoor() {
        hide();
        // TODO
    }

    JFileChooser openDialog;
    private JFileChooser getOpenDialog() {
        if (openDialog == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // TODO set filters

            openDialog = chooser;
        }
        return openDialog;
    }

    private void open() {
        hide();
        // TODO open file dialog
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = getOpenDialog();
            int ret = chooser.showOpenDialog(null);
            if (JFileChooser.APPROVE_OPTION == ret) {
                File file = chooser.getSelectedFile();
                // TODO
            }
        });
    }

    private void open(String path) {
        hide();
        // TODO open recent blueprint
    }

    private void save() {
        hide();
        // TODO open file dialog
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            chooser.showSaveDialog(null);
        });
    }

    private void saveAs() {
        hide();
        // TODO open file dialog
    }

    private void exit() {
        hide();
        app.stop();
    }
}
