package ffxiv.housim.graphics.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowRenderer;

import java.awt.*;

/**
 * 灯光模块
 * 
 * @author yanmoayuan
 *
 */
public class LightState extends BaseAppState {

    private Node rootNode;

    // 光源点
    private Node node = new Node("LightSources");

    private ViewPort viewPort;
    private AssetManager assetManager;

    // 光源
    private AmbientLight al;
    private PointLight pl;

    @Override
    protected void initialize(Application app) {

        viewPort = app.getViewPort();
        viewPort.setBackgroundColor(new ColorRGBA(0.75f, 0.8f, 0.9f, 1f));
        
        assetManager = app.getAssetManager();

        rootNode = ((SimpleApplication) app).getRootNode();

        /**
         * 光源
         */
        // 环境光
        al = new AmbientLight(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));

        // 点光源
        Vector3f position = new Vector3f(4, 10, 5);
        pl = new PointLight(position, new ColorRGBA(0.5f, 0.5f, 0.5f, 0.7f));
        pl.setRadius(100);

        PointLightShadowRenderer plsr = new PointLightShadowRenderer(assetManager, 1024);
        plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        plsr.setLight(pl);

        app.getViewPort().addProcessor(plsr);

        lightSource(position, pl.getColor());
    }

    /**
     * 创建一个小球，表示光源的位置。
     */
    private void lightSource(Vector3f position, ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        mat.setColor("GlowColor", color);

        Geometry geom = new Geometry("LightSource", new Sphere(6, 12, 0.2f));
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        geom.setShadowMode(ShadowMode.Off);

        node.attachChild(geom);
    }

    @Override
    protected void cleanup(Application app) {}

    @Override
    protected void onEnable() {
        // 添加光源
        rootNode.addLight(al);
        rootNode.addLight(pl);

        // 添加光源节点
        rootNode.attachChild(node);
    }

    @Override
    protected void onDisable() {
        // 移除光源节点
        node.removeFromParent();
        
        // 移除光源
        rootNode.removeLight(al);
        rootNode.removeLight(pl);
    }

}
