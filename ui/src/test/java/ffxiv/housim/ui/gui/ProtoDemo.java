//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ffxiv.housim.ui.gui;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.ColorChooser;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ListBox;
import com.simsilica.lemur.OptionPanelState;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedList;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

public class ProtoDemo extends SimpleApplication {
    private ListBox<String> listBox;
    private VersionedList<String> testList = new VersionedList();

    public static void main(String... args) {
        ProtoDemo main = new ProtoDemo();
        main.start();
    }

    public ProtoDemo() {
        super(new AppState[]{new StatsAppState(), new DebugKeysAppState(), new BasicProfilerState(false), new OptionPanelState("glass"), new ScreenshotAppState("", System.currentTimeMillis())});
    }

    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        Container window = new Container("glass");
        window.addChild(new Label("Test List", new ElementId("title"), "glass"), new Object[0]);

        for(int i = 0; i < 10; ++i) {
            this.testList.add("Item " + (i + 1));
        }

        this.listBox = new ListBox(this.testList, "glass");
        window.addChild(this.listBox, new Object[0]);
        Action add = new Action("Add") {
            public void execute(Button b) {
                ProtoDemo.this.testList.add("New Item " + (ProtoDemo.this.testList.size() + 1));
            }
        };
        final Action delete = new Action("Delete") {
            public void execute(Button b) {
                Integer selected = ProtoDemo.this.listBox.getSelectionModel().getSelection();
                if (selected != null && selected < ProtoDemo.this.testList.size()) {
                    ProtoDemo.this.testList.remove(selected);
                }

            }
        };
        final Action cancel = new Action("Cancel") {
            public void execute(Button b) {
            }
        };
        Action safeDelete = new Action("Safe Delete") {
            public void execute(Button b) {
                Integer selected = ProtoDemo.this.listBox.getSelectionModel().getSelection();
                if (selected != null && selected < ProtoDemo.this.testList.size()) {
                    String val = (String)ProtoDemo.this.testList.get(selected);
                    OptionPanelState ops = (OptionPanelState)ProtoDemo.this.stateManager.getState(OptionPanelState.class);
                    ops.show("Delete", "Really delete '" + val + "'?", new Action[]{delete, cancel});
                }
            }
        };
        Container buttons = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Even, FillMode.Even));
        window.addChild(buttons, new Object[0]);
        buttons.addChild(new ActionButton(add, "glass"), new Object[0]);
        buttons.addChild(new ActionButton(safeDelete, "glass"), new Object[0]);
        buttons.addChild(new ActionButton(delete, "glass"), new Object[0]);
        window.setLocalTranslation(300.0F, 600.0F, 0.0F);
        this.guiNode.attachChild(window);
        window = new Container("glass");
        window.addChild(new Label("Test Color Chooser", new ElementId("title"), "glass"), new Object[0]);
        ColorChooser colors = window.addChild(new ColorChooser("glass"), new Object[0]);
        colors.setPreferredSize(new Vector3f(300.0F, 90.0F, 0.0F));
        window.setLocalTranslation(100.0F, 400.0F, 0.0F);
        this.guiNode.attachChild(window);
    }
}
