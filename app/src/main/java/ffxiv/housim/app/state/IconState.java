package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import ffxiv.housim.graphics.factory.TextureFactory;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import ffxiv.housim.saintcoinach.utils.IconHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/4
 */
@Slf4j
public class IconState extends BaseAppState {

    Application app;
    InputManager inputManager;
    AssetManager assetManager;

    PackCollection packs;

    Node thisRoot = new Node("IconRoot");

    int col = 20;
    int row = 20;
    Material[] mats = new Material[col * row];

    int index;

    public IconState(PackCollection packs) {
        this.packs = packs;
    }

    @Override
    protected void initialize(Application app) {

        this.app = app;
        inputManager = app.getInputManager();
        assetManager = app.getAssetManager();

        if (app instanceof SimpleApplication simpleApp) {
            Node rootNode = simpleApp.getRootNode();
            rootNode.attachChild(thisRoot);
        }

        index = 130000;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                int n = x + y * col;
                Material mat = mats[n] = new Material(assetManager, Materials.UNSHADED);
                mat.setFloat("AlphaDiscardThreshold", 0.5f);
                Geometry geom = new Geometry("Icon" + n);
                geom.setMesh(new Quad(1, 1, true));
                geom.setMaterial(mat);
                thisRoot.attachChild(geom);

                geom.move(x - col / 2, row / 2 - y, 0);
            }
        }


        initInput();
        reload();
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

    private void initInput() {
        inputManager.addMapping("F_LEFT", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("F_RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("F_PICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {

                if (isPressed) {
                    switch (name) {
                        case "F_RIGHT": {
                            index += mats.length;
                            reload();
                            break;
                        }
                        case "F_LEFT": {
                            index -= mats.length;
                            reload();
                            break;
                        }
                    }
                }
            }
        }, "F_LEFT", "F_RIGHT");
    }

    private void reload() {
        Runnable runnable = () -> {
            log.info("loading: {}", index);
            for (int y = 0; y < row; y++) {
                for (int x = 0; x < col; x++) {
                    int n = x + y * col;
                    ImageFile imageFile = IconHelper.getIcon(packs, index + n);
                    Texture2D tex;
                    if (imageFile != null) {
                        tex = TextureFactory.get(imageFile);
                        tex.setMagFilter(Texture.MagFilter.Bilinear);
                    } else {
                        tex = null;
                    }
                    app.enqueue(() -> {
                        mats[n].setTexture("ColorMap", tex);
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
}
