package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.jme3.system.lwjgl.LwjglWindow;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class TestRestartContext extends SimpleApplication {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        //settings.setUseRetinaFrameBuffer(true);
        settings.setResizable(true);

        TestRestartContext app = new TestRestartContext();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        flyCam.setDragToRotate(true);
    }

    int frame = 0;
    @Override
    public void simpleUpdate(float tpf) {
        if (frame < 10) {
            frame++;
            int w = -1;
            int h = -1;
            if (context instanceof LwjglWindow) {
                LwjglWindow lwjgl = (LwjglWindow) context;

                long window = lwjgl.getWindowHandle();

                int[] width = new int[1];
                int[] height = new int[1];
                GLFW.glfwGetFramebufferSize(window, width, height);

                w = width[0];
                h = height[0];
                log.info("frame#{}: cam res={}x{}, fbSize={}x{}", frame, cam.getWidth(), cam.getHeight(), w, h);
            }

            if (frame == 3) {
                if (h != -1 && w != -1) {
                    reshape(w, h);
                }
            }

            if (frame == 5) {
                context.restart();
            }

//            if (frame == 7) {
//                if (h != -1 && w != -1) {
//                    reshape(w, h);
//                }
//            }
        }
    }
}
