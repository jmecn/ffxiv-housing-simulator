package com.jayfella.lemur.window;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.ListBox;
import com.simsilica.lemur.style.BaseStyles;

public class TestWindow extends SimpleApplication {

    public static void main(String[] args) {
        TestWindow app = new TestWindow();
        app.start();
    }

    private SimpleWindowManager windowManager;

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

        windowManager = new SimpleWindowManager();
        stateManager.attach(windowManager);
    }

    int i = 0;
    public void simpleUpdate(float tpf) {

        if (windowManager.isInitialized() && i != 1) {

            ListBox<String> list = new ListBox<>();
            list.setSize(new Vector3f(200, 400, 1));

            JmeWindow window = new JmeWindow("Test Window", list);

            // create a window with any generic Panel content
            windowManager.add(window);

            // show a dialog
            windowManager.showDialog(new JmeDialog(
                    "Test Dialog 2",
                    "I am a generic information dialog."));
            i++;
        }
    }
}
