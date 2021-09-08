package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.FrameBuffer;
import ffxiv.housim.app.state.indoor.IndoorState;
import ffxiv.housim.app.state.indoor.InteriorState;
import ffxiv.housim.ui.lemur.menubar.*;
import lombok.extern.slf4j.Slf4j;

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
public class MainMenu extends BaseAppState implements SceneProcessor {

    // Resource bundle for i18n.
    ResourceBundle i18n = ResourceBundle.getBundle("ffxiv.housim.i18n/MainMenu");

    private Node guiNode;

    private Camera cam;

    private Application app;

    private AppStateManager stateManager;

    private LemurMenuBar menuBar;

    @Override
    protected void initialize(Application app) {

        this.app = app;
        this.cam = app.getCamera();
        this.stateManager = app.getStateManager();

        if (app instanceof SimpleApplication simpleApp) {
            guiNode = simpleApp.getGuiNode();
        }

        app.getViewPort().addProcessor(this);
        final boolean horizontal = true;

        menuBar = new LemurMenuBar(100, horizontal, cam);
        guiNode.attachChild(menuBar.getSpatial());

        menuBar.setLocation(0, cam.getHeight());

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

        fileMenu.add(new MenuSeparator());

        // File -> Exit
        MenuItem exitMenuItem = fileMenu.add(new MenuItem(i18n.getString("file.exit")));
        exitMenuItem.addClickCommand(source -> exit());

        // Edit Menu
        Menu editMenu = menuBar.add(new Menu(i18n.getString("cfg")));

        // Edit -> Checkbox
        CheckboxMenuItem checkboxMenuItem = editMenu.add(new CheckboxMenuItem(i18n.getString("cfg.bgm")));

        // Help
        Menu helpMenu = menuBar.add(new Menu(i18n.getString("help")));
        MenuItem aboutMenuItem = helpMenu.add(new MenuItem(i18n.getString("help.about")));

        // finally
        // remove this line if you don't want it stretched.
        menuBar.setPreferredWidth(cam.getWidth());
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

    @Override
    public void initialize(RenderManager rm, ViewPort vp) { }

    @Override
    public void reshape(ViewPort vp, int w, int h) {
        menuBar.setLocation(0, h);
        menuBar.setPreferredWidth(w);
    }

    @Override
    public void preFrame(float tpf) { }

    @Override
    public void postQueue(RenderQueue rq) { }

    @Override
    public void postFrame(FrameBuffer out) { }

    @Override
    public void setProfiler(AppProfiler profiler) {  }

    private void newIndoor() {
        // TODO
        IndoorState state = stateManager.getState(IndoorState.class);
        if (state == null) {
            state = new IndoorState();
            stateManager.attach(state);
            stateManager.attach(new InteriorState());
        }

    }

    private void newOutdoor() {
        // TODO
    }

    private void open() {
        // TODO open file dialog
    }

    private void open(String path) {
        // TODO open recent blueprint
    }

    private void save() {
        // TODO open file dialog
    }

    private void saveAs() {
        // TODO open file dialog
    }

    private void exit() {
        app.stop();
    }
}
