package ffxiv.housim.app.factory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import ffxiv.housim.saintcoinach.io.Hash;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.material.MaterialDefinition;
import ffxiv.housim.saintcoinach.material.MaterialTextureParameter;
import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.material.shpk.Parameter;
import ffxiv.housim.saintcoinach.material.shpk.ParameterType;
import ffxiv.housim.saintcoinach.material.shpk.ShPkFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
public class MaterialFactory {

    @Setter
    static PackCollection packs;
    @Setter
    static AssetManager assetManager;


    static Cache<Integer, Material> CACHE;
    static {
        CACHE = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(3600))
                .softValues()
                .build();
    }


    public static Material build(String name) {
        int hash = Hash.compute(name);
        try {
            return CACHE.get(hash, () -> innerBuild(name));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return innerBuild(name);
        }
    }
    public static Material build(MaterialDefinition matDef) {
        int hash = Hash.compute(matDef.getName());
        try {
            return CACHE.get(hash, () -> innerBuild(matDef));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return innerBuild(matDef);
        }
    }

    private static Material innerBuild(String name) {
        log.info("load mtrl {}", name);
        PackFile file = packs.tryGetFile(name);
        if (file != null) {
            return buildLightingMat(new ffxiv.housim.saintcoinach.material.Material(null, file, ImcVariant.DEFAULT));
        }
        return colorMaterial(ColorRGBA.Blue);
    }

    private static Material innerBuild(MaterialDefinition matDef) {
        log.debug("load mtrl {}", matDef.getName());
        Material mat = buildLightingMat(matDef);
        // Material mat = buildShowNormal(matDef);
        return mat;
    }

    public static Material buildShowNormal(MaterialDefinition matDef) {
        Material mat = new Material(assetManager, "MatDefs/Tool/ShowNormals.j3md");

        ffxiv.housim.saintcoinach.material.Material m = matDef.get();

        String shader = m.getShader();

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
                if (name.contains("ColorMap0") && "bg.shpk".equals(shader)) {
                    mat.setFloat("AlphaDiscardThreshold", e.alphaDiscard);
                }
                // TODO handle dual
            }
        }

        log.debug("colorSet size:{}, unknownSize:{}, colorSet count:{}, colorSet names:{}", m.getColorSetDataSize(), m.getUnknownSize(), m.getColorSetCount(), m.getColorSets());
        return mat;
    }

    public static Material buildLightingMat(MaterialDefinition matDef) {
        return buildLightingMat(matDef.get());
    }

    public static Material buildLightingMat(ffxiv.housim.saintcoinach.material.Material m) {

        ShPkFile shPk = m.getShPk();
        String shader = m.getShader();

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        Material mat = new Material(assetManager, Materials.LIGHTING);

        if ("bgcolorchange.shpk".equals(shader)) {
            return bgcolorchange(m);
        } else if ("bg.shpk".equals(shader)) {
            return bg(m);
        } else if ("water.shpk".equals(shader)) {
            return water(m);
        } else {
            log.warn("unknown shader:{}", shader);
            return colorMaterial(ColorRGBA.randomColor());
        }
    }


    public static Material bg(ffxiv.housim.saintcoinach.material.Material m) {

        ShPkFile shPk = m.getShPk();
        String shader = m.getShader();

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        Material mat = new Material(assetManager, Materials.LIGHTING);
        //Material mat = new Material(assetManager, "MatDefs/Shader/Lighting.j3md");
        mat.setBoolean("VertexLighting", true);

        for (MaterialTextureParameter e : matParams) {
            Parameter param = shPk.getParameter(e.getParameterId());
            ImageFile image = textureFiles[e.getTextureIndex()];
            log.debug("param:{}, image:{}", param, image.getPath());
            if (param.getType() == ParameterType.Sampler) {
                Texture2D texture = TextureFactory.get(image);
                String name = param.getName().substring(2);
                if (name.endsWith("ColorMap0")) {
                    mat.setTexture("DiffuseMap", texture);
                    mat.setFloat("AlphaDiscardThreshold", e.alphaDiscard);
                    log.info("alphaDiscard:{}", e.alphaDiscard);
                } else if (name.endsWith("NormalMap0")) {
                    mat.setTexture("NormalMap", texture);
                } else if (name.endsWith("SpecularMap0")) {
                    mat.setTexture("SpecularMap", texture);
                }
            } else {
                log.info("ignore param:{}", param.getName());
            }
        }

        log.debug("colorSet size:{}, unknownSize:{}, colorSet count:{}, colorSet names:{}, data size:{}", m.getColorSetDataSize(), m.getUnknownSize(), m.getColorSetCount(), m.getColorSets(), m.getDataSize());
        return mat;
    }


    public static Material water(ffxiv.housim.saintcoinach.material.Material m) {

        ShPkFile shPk = m.getShPk();
        String shader = m.getShader();

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        Material mat = new Material(assetManager, Materials.LIGHTING);

        ColorRGBA color = new ColorRGBA(1f, 1f, 1f, 1f);
        float[] materialParameter = m.getMaterialParameter();
        if (materialParameter.length > 4) {
            color.set(materialParameter[0], materialParameter[1], materialParameter[2], materialParameter[3]);
        }
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);
        mat.setColor("Specular", ColorRGBA.White);
        //mat.setBoolean("UseMaterialColors", true);

        for (MaterialTextureParameter e : matParams) {
            Parameter param = shPk.getParameter(e.getParameterId());
            ImageFile image = textureFiles[e.getTextureIndex()];
            log.debug("param:{}, image:{}", param, image.getPath());
            if (param.getType() == ParameterType.Sampler) {
                Texture2D texture = TextureFactory.get(image);
                String name = param.getName().substring(2);
                if (name.endsWith("WaveMap")) {
                    mat.setTexture("DiffuseMap", texture);
                    mat.setTexture("NormalMap", texture);
                }
            } else {
                log.info("ignore param:{}", param.getName());
            }
        }

        log.debug("colorSet size:{}, unknownSize:{}, colorSet count:{}, colorSet names:{}, data size:{}", m.getColorSetDataSize(), m.getUnknownSize(), m.getColorSetCount(), m.getColorSets(), m.getDataSize());
        return mat;
    }

    public static Material bgcolorchange(ffxiv.housim.saintcoinach.material.Material m) {
        ShPkFile shPk = m.getShPk();

        ImageFile[] textureFiles = m.getTextureFiles();
        MaterialTextureParameter[] matParams = m.getTextureParameters();

        Material mat = new Material(assetManager, Materials.LIGHTING);
        //Material mat = new Material(assetManager, "MatDefs/Shader/Lighting.j3md");
        mat.setBoolean("VertexLighting", true);

        ColorRGBA color = new ColorRGBA(1f, 1f, 1f, 1f);
        float[] materialParameter = m.getMaterialParameter();
        if (materialParameter.length > 4) {
            color.set(materialParameter[0], materialParameter[1], materialParameter[2], materialParameter[3]);
        }
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setBoolean("UseMaterialColors", true);

        for (MaterialTextureParameter e : matParams) {
            Parameter param = shPk.getParameter(e.getParameterId());
            ImageFile image = textureFiles[e.getTextureIndex()];
            log.debug("param:{}, image:{}", param, image.getPath());
            if (param.getType() == ParameterType.Sampler) {
                Texture2D texture = TextureFactory.get(image);
                String name = param.getName().substring(2);
                if (name.endsWith("ColorMap0")) {
                    mat.setTexture("DiffuseMap", texture);
                } else if (name.endsWith("NormalMap0")) {
                    log.info("image:{}", image.getFormat());
                    mat.setTexture("NormalMap", texture);
                } else if (name.endsWith("SpecularMap0")) {
                    mat.setTexture("SpecularMap", texture);
                }
            } else {
                log.info("ignore param:{}", param.getName());
            }
        }
        log.debug("colorSet size:{}, unknownSize:{}, colorSet count:{}, colorSet names:{}, data size:{}", m.getColorSetDataSize(), m.getUnknownSize(), m.getColorSetCount(), m.getColorSets(), m.getDataSize());
        return mat;
    }

    public static Material colorMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setWireframe(true);
        return mat;
    }
}
