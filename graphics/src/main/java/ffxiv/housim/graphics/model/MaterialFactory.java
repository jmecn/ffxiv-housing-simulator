package ffxiv.housim.graphics.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import ffxiv.housim.graphics.texture.TextureFactory;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.material.MaterialTextureParameter;
import ffxiv.housim.saintcoinach.material.shpk.Parameter;
import ffxiv.housim.saintcoinach.material.shpk.ParameterType;
import ffxiv.housim.saintcoinach.material.shpk.ShPkFile;
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
        return buildShowNormal(matDef);
    }

    public static Material buildShowNormal(MaterialDefinition matDef) {
        Material mat = new Material(assetManager, "MatDefs/Tool/ShowNormals.j3md");

        ffxiv.housim.saintcoinach.material.Material m = matDef.get();

        ShPkFile shPk = m.getShPk();

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        for (MaterialTextureParameter e : matParams) {
            Parameter param = shPk.getParameter(e.getParameterId());
            ImageFile image = textureFiles[e.getTextureIndex()];
            log.debug("param:{}, image:{}", param, image.getPath());
            if (param.getType() == ParameterType.Sampler) {
                Texture texture = TextureFactory.get(image);
                String name = param.getName().substring(2);
                mat.setTexture(name, texture);
                mat.setBoolean("Has" + name, true);
            }
        }

        log.info("colorSet size:{}", m.getColorSetDataSize());
        return mat;
    }

    public static Material buildLightingMat(MaterialDefinition matDef) {
        ffxiv.housim.saintcoinach.material.Material m = matDef.get();

        ShPkFile shPk = m.getShPk();

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        Material mat = new Material(assetManager, Materials.LIGHTING);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.Yellow);

        for (MaterialTextureParameter e : matParams) {
            Parameter param = shPk.getParameter(e.getParameterId());
            ImageFile image = textureFiles[e.getTextureIndex()];
            log.debug("param:{}, image:{}", param, image.getPath());
            if (param.getType() == ParameterType.Sampler) {
                Texture texture = TextureFactory.get(image);
                String name = param.getName().substring(2);
                if (name.endsWith("ColorMap0")) {
                    mat.setTexture("DiffuseMap", texture);
                } else if (name.endsWith("NormalMap0")) {
                    //mat.setTexture("NormalMap", texture);
                } else if (name.endsWith("SpecularMap0")) {
                    mat.setTexture("SpecularMap", texture);
                }
            } else {
                log.info("ignore param:{}", param.getName());
            }
        }

        log.info("colorSet size:{}", m.getColorSetDataSize());
        return mat;
    }

    public static Material colorMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setWireframe(true);
        return mat;
    }
}
