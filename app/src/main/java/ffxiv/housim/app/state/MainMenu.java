package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.FrameBuffer;
import ffxiv.housim.ui.lemur.menubar.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
@Slf4j
public class MainMenu extends BaseAppState implements SceneProcessor {

    private Node guiNode;

    private Camera cam;

    private Application app;

    private LemurMenuBar menuBar;

    @Override
    protected void initialize(Application app) {

        this.app = app;
        this.cam = app.getCamera();

        if (app instanceof SimpleApplication simpleApp) {
            guiNode = simpleApp.getGuiNode();
        }

        app.getViewPort().addProcessor(this);
        final boolean horizontal = true;

        menuBar = new LemurMenuBar(100, horizontal, cam);
        guiNode.attachChild(menuBar.getSpatial());

        menuBar.setLocation(0, cam.getHeight());

        // File Menu
        Menu fileMenu = menuBar.add(new Menu("文件"));

        // File -> New
        Menu newMenu = fileMenu.add(new Menu("新建 >"));

        MenuItem newIndoor = newMenu.add(new MenuItem("家具装修蓝图"));
        newIndoor.addClickCommand(source -> newIndoor());

        MenuItem newOutdoor = newMenu.add(new MenuItem("庭具装修蓝图"));
        newOutdoor.addClickCommand(source -> newOutdoor());
        newOutdoor.setEnabled(false);

        // File -> Open
        MenuItem openMenuItem = fileMenu.add(new MenuItem("打开..."));
        openMenuItem.addClickCommand(source -> open());

        Menu openRecentMenu = fileMenu.add(new Menu("打开最近 >"));
        setRecentMenu(openRecentMenu);

        MenuItem save = fileMenu.add(new MenuItem("保存..."));
        save.addClickCommand(source -> save());
        save.setEnabled(false);

        MenuItem saveAs = fileMenu.add(new MenuItem("另存为..."));
        saveAs.addClickCommand(source -> saveAs());
        saveAs.setEnabled(false);

        fileMenu.add(new MenuSeparator());

        // File -> Exit
        MenuItem exitMenuItem = fileMenu.add(new MenuItem("退出"));
        exitMenuItem.addClickCommand(source -> exit());

        // Edit Menu
        Menu editMenu = menuBar.add(new Menu("设置"));

        // Edit -> Checkbox
        CheckboxMenuItem checkboxMenuItem = editMenu.add(new CheckboxMenuItem("TODO"));

        // Help
        Menu helpMenu = menuBar.add(new Menu("帮助"));
        MenuItem aboutMenuItem = helpMenu.add(new MenuItem("关于..."));

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
