package ffxiv.housim.app;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.system.lwjgl.LwjglContext;
import com.jme3.system.lwjgl.LwjglWindow;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseAppState;
import com.simsilica.lemur.event.MouseEventControl;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class Checker3D extends SimpleApplication {
    static int WIDTH = 1280;
    static int HEIGHT = 720;

    public static void main(String[] args) {

        AppSettings settings = new AppSettings(true);
        settings.setResolution(WIDTH, HEIGHT);
        settings.setResizable(true);
        //settings.setUseRetinaFrameBuffer(true);
        settings.setSamples(4);

        Checker3D app = new Checker3D();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);

        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        cam.setLocation(new Vector3f(3, 6, 20));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        inputManager.setCursorVisible(true);

        // create 16*9 quads as a checkerboard.
        Mesh mesh = new Quad(1, 1);
        for (int y=0; y<9; y++) {
            for (int x=0; x<16; x++) {

                ColorRGBA color;
                if ((x + y) % 2 == 0) {
                    color = new ColorRGBA(1f - x / 16f, y / 9f, 1f, 1f);
                } else {
                    color = new ColorRGBA(x / 16f, 1f - y / 9f, 1f, 1f);
                }

                Material mat = new Material(assetManager, Materials.UNSHADED);
                mat.setColor("Color", color);

                Geometry geom = new Geometry("#("+x+","+y+")", mesh);
                geom.setMaterial(mat);
                geom.move(x - 8, y - 6, 0);
                rootNode.attachChild(geom);

                MouseEventControl.addListenersToSpatial(geom, new DefaultMouseListener() {
                    @Override
                    protected void click( MouseButtonEvent event, Spatial target, Spatial capture ) {
                        Vector2f cursor = inputManager.getCursorPosition();
                        System.out.printf("geom:%s, cursor:%s\n", target.getName(), cursor);

                        // get window handle
                        LwjglWindow context = (LwjglWindow) getContext();
                        long window = context.getWindowHandle();
                        // get window content scale
                        float[] xScale = new float[1];
                        float[] yScale = new float[1];
                        GLFW.glfwGetWindowContentScale(window, xScale, yScale);
                        // get cursor pos
                        double[] xPos = new double[1];
                        double[] yPos = new double[1];
                        GLFW.glfwGetCursorPos(window, xPos, yPos);
                        // real pos = pos * scale
                        double x = xPos[0] * xScale[0];
                        double y = yPos[0] * yScale[0];
                        System.out.printf("window content scale:(%.2f, %.2f), cursor pos:(%.4f, %.4f), content pos:(%.2f, %.2f)\n", xScale[0], yScale[0], xPos[0], yPos[0], x, y);
                    }

                    @Override
                    public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture ) {
                        Material m = ((Geometry)target).getMaterial();
                        m.setColor("Color", ColorRGBA.Yellow);
                    }

                    @Override
                    public void mouseExited( MouseMotionEvent event, Spatial target, Spatial capture ) {
                        Material m = ((Geometry)target).getMaterial();
                        m.setColor("Color", color);
                    }
                });

            }
        }
    }

}
