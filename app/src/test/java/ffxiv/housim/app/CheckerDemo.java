package ffxiv.housim.app;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.lwjgl.LwjglWindow;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckerDemo extends SimpleApplication {
    static int WIDTH = 1280;
    static int HEIGHT = 720;
    static int SIZE = 80;// (1280, 720) = 80 * (16, 9)

    public static void main(String[] args) {

        AppSettings settings = new AppSettings(true);
        settings.setResolution(WIDTH, HEIGHT);
        settings.setResizable(true);
        //settings.setUseRetinaFrameBuffer(true);
        settings.setSamples(4);

        CheckerDemo app = new CheckerDemo();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        stateManager.getState(MouseAppState.class);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        // create 16*9 quads as a checkerboard.
        Mesh mesh = new Quad(SIZE, SIZE);
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
                geom.move(x * SIZE, y * SIZE, 0);
                guiNode.attachChild(geom);

                MouseEventControl.addListenersToSpatial(geom, new DefaultMouseListener() {
                    @Override
                    protected void click( MouseButtonEvent event, Spatial target, Spatial capture ) {
                        Vector2f cursor = inputManager.getCursorPosition();
                        System.out.printf("geom:%s, cursor:%s, cam:%s\n", target.getName(), cursor, guiViewPort.getCamera());
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

    int frame = 0;

    @Override
    public void simpleUpdate(float tpf) {
        if (frame < 10) {
            frame++;

            Vector2f scale = new Vector2f(1f, 1f);
            JmeContext context = getContext();
            if (context instanceof LwjglWindow) {
                //((LwjglWindow) context).getWindowContentScale(scale);
            }
            log.info("frame:{}, scale:{}", frame, scale);

            if (frame == 2) {// update gui on the 2nd frame
                guiNode.setLocalScale(scale.x, scale.y, 1f);
            } else if (frame == 3) {
                //context.restart();
            }
        }
    }
}
