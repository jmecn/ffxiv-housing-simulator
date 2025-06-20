package ffxiv.housim.ui.lemur.menubar;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.CursorButtonEvent;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DefaultCursorListener;
import com.simsilica.lemur.style.BaseStyles;

public class TestMenuBar extends SimpleApplication {

    public static void main(String... args) {

        TestMenuBar testMenuBar = new TestMenuBar();

        AppSettings appSettings = new AppSettings(true);
        appSettings.setResolution(1280, 720);

        testMenuBar.setSettings(appSettings);
        testMenuBar.setShowSettings(false);
        testMenuBar.start();
    }

    private TestMenuBar() {
        super();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setDragToRotate(true);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

        createHorizontalMenu();
        createContextMenu();
    }

    private void createContextMenu() {

        Geometry cube = new Geometry("Cube", new Box(1,1,1));
        cube.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        rootNode.attachChild(cube);

        final boolean horizontal = false;

        LemurMenuBar contextMenu = new LemurMenuBar(901, horizontal, cam);

        // a click command that removes the context menu after an item is clicked.
        Command<Button> removeContextMenuCommand = source -> {
            contextMenu.hideAllMenus();
            contextMenu.getSpatial().removeFromParent();
        };

        MenuItem menuitem_1 = contextMenu.add(new MenuItem("Menu Item 1"));
        menuitem_1.addClickCommand(removeContextMenuCommand);

        MenuItem menuitem_2 = contextMenu.add(new MenuItem("Menu Item 2"));
        menuitem_2.addClickCommand(removeContextMenuCommand);

        contextMenu.add(new MenuSeparator());

        Menu menu = contextMenu.add(new Menu("Test Menu"));

        MenuItem submenu_1 = menu.add(new MenuItem("Sub Menu 1.1"));
        submenu_1.addClickCommand(removeContextMenuCommand);

        MenuItem submenu_2 = menu.add(new MenuItem("Sub Menu 1.2"));
        submenu_2.addClickCommand(removeContextMenuCommand);

        MenuItem submenu_3 = menu.add(new MenuItem("Sub Menu 1.3"));
        submenu_3.addClickCommand(removeContextMenuCommand);

        MenuItem rightSide_1 = contextMenu.add(new MenuItem("Right Side 1"), LemurMenuBar.Position.Right);
        rightSide_1.addClickCommand(removeContextMenuCommand);

        MenuItem rightSide_2 = contextMenu.add(new MenuItem("Right Side 2"), LemurMenuBar.Position.Right);
        rightSide_2.addClickCommand(removeContextMenuCommand);

        CursorEventControl.addListenersToSpatial(cube, new DefaultCursorListener() {

            @Override
            protected void click(CursorButtonEvent event, Spatial target, Spatial capture ) {

                // right-click
                if (event.getButtonIndex() == 1) {

                    // if the context menu is already visible remove any submenus that may be active.
                    if (contextMenu.getSpatial().getParent() != null) {
                        contextMenu.hideAllMenus();
                    }

                    // set the location to the position that was clicked.
                    guiNode.attachChild(contextMenu.getSpatial());
                    contextMenu.setLocation(event.getX(), event.getY());

                }

            }

        });


    }

    private void createHorizontalMenu() {

        final boolean horizontal = true;

        LemurMenuBar menuBar = new LemurMenuBar(100, horizontal, cam);
        guiNode.attachChild(menuBar.getSpatial());

        menuBar.setLocation(0, cam.getHeight());

        // File Menu
        Menu fileMenu = menuBar.add(new Menu("File"));

        // File -> Exit
        MenuItem exitMenuItem = fileMenu.add(new MenuItem("Exit"));
        exitMenuItem.addClickCommand(source -> stop());

        // Edit Menu
        Menu editMenu = menuBar.add(new Menu("Edit"));

        // Edit -> Checkbox
        CheckboxMenuItem checkboxMenuItem = editMenu.add(new CheckboxMenuItem("CheckBox"));

        // Edit -> Disabled Checkbox
        CheckboxMenuItem disabledCheckboxMenuItem = editMenu.add(new CheckboxMenuItem("Disabled Checkbox"));
        disabledCheckboxMenuItem.setEnabled(false);

        editMenu.add(new MenuSeparator());

        // Edit -> MenuItem
        MenuItem menuItem = editMenu.add(new MenuItem("Menu Item"));

        // Edit -> Disabled MenuItem
        MenuItem disabledMenuItem = editMenu.add(new MenuItem("Disabled Item"));
        disabledMenuItem.setEnabled(false);

        editMenu.add(new MenuSeparator());

        // Edit -> Sub Menu
        Menu submenu = editMenu.add(new Menu("Sub Menu"));
        submenu.add(new MenuItem("Sub Item 1.1"));
        submenu.add(new MenuItem("Sub Item 1.2"));
        submenu.add(new MenuItem("Sub Item 1.3"));

        // Edit -> Sub Menu -> Sub Menu 2
        Menu submenu2 = submenu.add(new Menu("Sub Menu 2"));
        submenu2.add(new MenuItem("Sub Item 2.1"));
        submenu2.add(new MenuItem("Sub Item 2.2"));
        submenu2.add(new MenuItem("Sub Item 2.3"));

        // Edit -> Disabled Sub Menu
        Menu subMenu3 = editMenu.add(new Menu("Disabled Sub Menu"));
        subMenu3.add(new MenuItem("Disabled Sub Item 1.1"));
        subMenu3.add(new MenuItem("Disabled Sub Item 1.2"));
        subMenu3.add(new MenuItem("Disabled Sub Item 1.3"));
        subMenu3.setEnabled(false);

        // Help
        Menu helpMenu = menuBar.add(new Menu("Help"), LemurMenuBar.Position.Right);
        MenuItem aboutMenuItem = helpMenu.add(new MenuItem("About..."));

        // finally
        // remove this line if you don't want it stretched.
        menuBar.setPreferredWidth(cam.getWidth());

    }

}