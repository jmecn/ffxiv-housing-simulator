package ffxiv.housim.app.gui;

import com.jayfella.lemur.menubar.LemurMenuBar;
import com.jayfella.lemur.menubar.Menu;
import com.jayfella.lemur.menubar.MenuItem;
import com.jayfella.lemur.menubar.MenuSeparator;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.CursorButtonEvent;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DefaultCursorListener;
import com.simsilica.lemur.style.BaseStyles;

public class TestMenuBar extends SimpleApplication {

    public static void main(String[] args) {
        TestMenuBar app = new TestMenuBar();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

        flyCam.setDragToRotate(true);

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

}