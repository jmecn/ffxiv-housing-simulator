package ffxiv.housim.app;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.demo.BasicDemo;
import com.simsilica.lemur.event.DefaultRawInputListener;

public class LemurBasicDemo {
    public static void main(String[] args) {

        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setUseRetinaFrameBuffer(true);
        settings.setResizable(true);

        BasicDemo app = new BasicDemo() {
            @Override
            public void simpleInitApp() {
            super.simpleInitApp();

            inputManager.addRawInputListener(new DefaultRawInputListener() {
                @Override
                public void onMouseButtonEvent(MouseButtonEvent evt) {
                    System.out.printf("The cursor click position:%s\n", inputManager.getCursorPosition());
                }
            });
            }
        };
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

}
