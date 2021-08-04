package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;

public class Main extends SimpleApplication {
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 0.85f, 1.0f));

        Material mat = new Material(assetManager, Materials.LIGHTING);
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setBoolean("UseMaterialColors", true);

        Geometry geom = new Geometry("cube");
        geom.setMesh(new Box(1, 1, 1));
        geom.setMaterial(mat);

        geom.addControl(new AbstractControl() {
            @Override
            protected void controlUpdate(float v) {
                spatial.rotate(0, FastMath.PI * v, 0);
            }

            @Override
            protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

            }
        });
        rootNode.attachChild(geom);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-3, -4, -5).normalizeLocal());
        dl.setColor(ColorRGBA.White);

        rootNode.addLight(dl);

        flyCam.setMoveSpeed(10f);
    }

    public void simpleUpdate(float tpf) {}
    public static void main(String[] args) {
        Main app = new Main();
        //app.setShowSettings(false);
        app.start(
        );
    }
}
