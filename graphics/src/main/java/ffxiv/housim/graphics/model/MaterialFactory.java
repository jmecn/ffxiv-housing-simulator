package ffxiv.housim.graphics.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import ffxiv.housim.graphics.texture.TextureFactory;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaterialFactory {

    @Setter
    static PackCollection packs;
    @Setter
    static AssetManager assetManager;

    public static Material build(MaterialDefinition matDef) {
        ffxiv.housim.saintcoinach.material.Material m = matDef.get();

        ImageFile file = m.getTextureFiles()[0];
        Texture diffuse = null;
        if (file != null) {
            diffuse = TextureFactory.get(file);
        }

        Material mat = new Material(assetManager, Materials.LIGHTING);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setBoolean("UseMaterialColors", true);
        // mat.setFloat("AlphaDiscardThreshold", 0.5f);
        if (diffuse != null) {
            mat.setTexture("DiffuseMap", diffuse);
        }
        return mat;
    }

    public static Material colorMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setWireframe(true);
        return mat;
    }
}
