package ffxiv.housim.app.state.indoor;

import com.jme3.anim.AnimComposer;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import ffxiv.housim.ui.lemur.window.SimpleWindowManager;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
@Slf4j
public class IndoorState extends BaseAppState {

    private SimpleWindowManager windowManager;
    private AssetManager assetManager;
    private Node rootNode;

    @Override
    protected void initialize(Application app) {

        assetManager = app.getAssetManager();

        if (app instanceof SimpleApplication simpleApp) {
            rootNode = simpleApp.getRootNode();
        }
        windowManager = app.getStateManager().getState(SimpleWindowManager.class);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }


    private void addRobot() {

        Material mat = new Material(assetManager, Materials.LIGHTING);
        mat.setColor("Diffuse", new ColorRGBA(0.5f, 0.5f, 0.5f, 1f));
        mat.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 0.5f, 1f));
        mat.setColor("Specular", new ColorRGBA(1f, 1f, 1f, 1f));
        mat.setBoolean("UseMaterialColors", true);
        mat.setFloat("Shininess", 32);

        Spatial robot = assetManager.loadModel("Model/s9_drone/scene.gltf");
        robot.scale(0.05f);

        rootNode.attachChild(robot);

        AtomicReference<AnimComposer> anim = new AtomicReference<>();
        robot.depthFirstTraversal(e -> {
            AnimComposer ac = e.getControl(AnimComposer.class);
            if (ac != null) {
                anim.set(ac);
                log.info("{}", ac);
            }
        });
        if (anim.get() != null) {
            AnimComposer ac = anim.get();
            ac.setCurrentAction("Take 01");
        }
    }
}
