package ffxiv.housim.app;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import ffxiv.housim.graphics.texture.TextureFactory;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.imaging.ImageFile;
import ffxiv.housim.saintcoinach.io.PackCollection;

import java.io.IOException;

public class Main extends SimpleApplication {

    PackCollection collection;
    private void initGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        collection = new PackCollection(gameDir + "/game/sqpack");
    }

    public void simpleInitApp() {
        initGameDir();

        ImageFile d = (ImageFile) collection.tryGetFile("bgcommon/hou/dyna/opt/wl/0002/texture/opt_wl_m0002_0b_d.tex");
        ImageFile n = (ImageFile) collection.tryGetFile("bgcommon/hou/dyna/opt/wl/0002/texture/opt_wl_m0002_0b_n.tex");
        ImageFile s = (ImageFile) collection.tryGetFile("bgcommon/hou/dyna/opt/wl/0002/texture/opt_wl_m0002_0b_s.tex");

        Texture diffuse = TextureFactory.get(d);
        Texture normal = TextureFactory.get(n);
        Texture specular = TextureFactory.get(s);

        ImageFile xinput = (ImageFile) collection.tryGetFile("common/font/fonticon_xinput.tex");
        Texture xinputTex = TextureFactory.get(xinput);

        ImageFile noise = (ImageFile) collection.tryGetFile("common/graphics/texture/-ch_ele_000af.tex");
        Texture noiseTex = TextureFactory.get(noise);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 0.85f, 1.0f));

        Material mat = new Material(assetManager, Materials.LIGHTING);
        //mat.setTexture("DiffuseMap", xinputTex);
        //mat.setTexture("DiffuseMap", noiseTex);
        //mat.setTexture("DiffuseMap", diffuse);
        mat.setTexture("DiffuseMap", specular);
        //mat.setTexture("NormalMap", normal);
        //mat.setTexture("SpecularMap", specular);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1f);
        //mat.setBoolean("UseMaterialColors", true);
        mat.setFloat("AlphaDiscardThreshold", 0.50f);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

        Geometry geom = new Geometry("cube");
        geom.setMesh(new Quad(4, 4));
        geom.setMaterial(mat);

        rootNode.attachChild(geom);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-3, -5, -4).normalizeLocal());
        dl.setColor(ColorRGBA.White);

        rootNode.addLight(dl);

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
    }

    public void simpleUpdate(float tpf) {

    }

    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("Final Fantasy XIV Housing Simulator v0.0.1-SNAPSHOT");
        setting.setResolution(1280, 720);
        setting.setResizable(true);
        setting.setFrameRate(60);
        setting.setSamples(4);
        // LWJGL-OpenGL2
        setting.setRenderer(AppSettings.LWJGL_OPENGL41);
        System.out.println(setting.getRenderer());

        Main app = new Main();
        app.setSettings(setting);
        app.start();
    }
}
