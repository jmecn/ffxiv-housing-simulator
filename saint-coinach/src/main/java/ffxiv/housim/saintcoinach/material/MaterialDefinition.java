package ffxiv.housim.saintcoinach.material;

import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelVariantIdentifier;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaterialDefinition {
    static PathExpander[] PathExpanders = new PathExpander[] {
            new PathExpander(
                    "^/(?<basename>mt_c(?<c>[0-9]{4})a(?<a>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/accessory/a${a}/material/v{0:D4}/${basename}.mtrl",
                    "chara/accessory/a${a}/material/v{0:D4}/staining/${basename}_s{1:D4}.mtrl",
                    true),
            new PathExpander(
                    "^/(?<basename>mt_c(?<c>[0-9]{4})b(?<b>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/human/c${c}/obj/body/b${b}/material/${basename}.mtrl",
                    null,
                    false),
            new PathExpander(
                    "^/(?<basename>mt_c(?<c>[0-9]{4})h(?<h>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/human/c${c}/obj/hair/h${h}/material/v{0:D4}/${basename}.mtrl",
                    null,
                    true),
            new PathExpander(
                    "^/(?<basename>mt_c(?<c>[0-9]{4})f(?<f>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/human/c${c}/obj/face/f${f}/material/${basename}.mtrl",
                    null,
                    false),
            new PathExpander(
                    "^/(?<basename>mt_c(?<c>[0-9]{4})t(?<t>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/human/c${c}/obj/tail/t${t}/material/${basename}.mtrl",
                    null,
                    false),
            new PathExpander(
                    "^/(?<basename>mt_c(?<c>[0-9]{4})e(?<e>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/equipment/e${e}/material/v{0:D4}/${basename}.mtrl",
                    "chara/equipment/e${e}/material/v{0:D4}/staining/${basename}_s{1:D4}.mtrl",
                    true),
            new PathExpander(
                    "^/(?<basename>mt_d(?<d>[0-9]{4})e(?<e>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/demihuman/d${d}/obj/equipment/e${e}/material/v{0:D4}/${basename}.mtrl",
                    null,
                    true),
            new PathExpander(
                    "^/(?<basename>mt_m(?<m>[0-9]{4})b(?<b>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/monster/m${m}/obj/body/b${b}/material/v{0:D4}/${basename}.mtrl",
                    null,
                    true),
            new PathExpander(
                    "^/(?<basename>mt_w(?<w>[0-9]{4})b(?<b>[0-9]{4})_(?<suffix>[^\\.]+))\\.mtrl$",
                    "chara/weapon/w${w}/obj/body/b${b}/material/v{0:D4}/${basename}.mtrl",
                    "chara/weapon/w${w}/obj/body/b${b}/material/v{0:D4}/staining/${basename}_s{1:D4}.mtrl",
                    true),
    };

    private ExpandResult tryExpand(String name) {
        log.info("try expand: {}", name);

        if (name == null || name.isEmpty()) {
            return null;
        }

        int i = name.lastIndexOf('/');
        if (i < 0) {
            return null;
        }

        String search = name.substring(i);

        ExpandResult result = new ExpandResult();
        for (var pe : PathExpanders) {
            if (pe.tryExpand(search, result)) {
                return result;
            }
        }
        return null;
    }

    @Getter
    private String defaultPath;
    private String pathFormat;
    private String stainedPathFormat;
    private boolean stainsAvailable;
    private boolean variantsAvailable;

    private PackCollection packs;

    @Getter
    private ModelDefinition definition;

    @Getter
    private String name;

    private int index;

    public MaterialDefinition(ModelDefinition definition, int index) {
        this.definition = definition;
        this.index = index;
        // TODO
        name = definition.getMaterialName(index);
        packs = definition.getFile().getPack().getCollection();

        if (packs.fileExists(name)) {
            defaultPath = name;
            variantsAvailable = false;
            stainedPathFormat = pathFormat = null;
        } else {
            ExpandResult result = tryExpand(name);
            if (result == null) {
                log.warn("No materials found:{}", name);
                return;
            }

            pathFormat = result.pathFormat;
            stainedPathFormat = result.stainedPathFormat;
            variantsAvailable = result.variantsAvailable;

            stainsAvailable = stainedPathFormat != null;
            if (variantsAvailable) {
                defaultPath = String.format(pathFormat, 0);
            } else {
                defaultPath = pathFormat;
            }
        }
    }

    public Material get(ModelVariantIdentifier variantId) {
        if (variantId.stainKey != null && stainsAvailable) {
            return get(variantId.imcVariant, variantId.stainKey);
        }

        return get(variantId.imcVariant);
    }

    public Material get() {
        var path = defaultPath;
        return create(path, ImcVariant.DEFAULT);
    }

    public Material get(ImcVariant variant) {
        String path = defaultPath;
        if (variantsAvailable) {
            path = String.format(pathFormat, variant.materialNum & 0xFF);
        }
        return create(path, variant);
    }

    public Material get(ImcVariant variant, int stainKey) {
        if (!stainsAvailable) {
            throw new UnsupportedOperationException("Stain is not available");
        }

        String path = String.format(stainedPathFormat, variant.materialNum & 0xFF, stainKey);
        return create(path, variant);
    }

    private Material create(String path, ImcVariant variant) {
        PackFile file = packs.tryGetFile(path);
        return new Material(this, file, variant);
    }
}
