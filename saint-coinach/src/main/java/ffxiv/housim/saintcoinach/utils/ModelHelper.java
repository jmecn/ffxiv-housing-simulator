package ffxiv.housim.saintcoinach.utils;

import ffxiv.housim.saintcoinach.db.xiv.entity.ModelChara;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.material.imc.ImcFile;
import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.math.XivQuad;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
@Slf4j
public final class ModelHelper {

    @Getter
    private final String imcFileFormat;
    @Getter
    private final int imcPartKey;
    @Getter
    private final String modelFileFormat;
    @Getter
    private final int variantIndexWord;

    public ModelHelper(String imcFileFormat, int imcPartKey, String modelFileFormat, int variantIndexWord) {
        this.imcFileFormat = imcFileFormat;
        this.imcPartKey = imcPartKey;
        this.modelFileFormat = modelFileFormat;
        this.variantIndexWord = variantIndexWord;
    }

    // EquipSlot ModelHelper
    public static final Map<Integer, ModelHelper> MODEL_HELPERS = new HashMap<>();
    static {
        MODEL_HELPERS.put(  // Main hand
            0, new ModelHelper(
                    "chara/weapon/w%1$04d/obj/body/b%2$04d/b%2$04d.imc",
                    0,
                    "chara/weapon/w%1$04d/obj/body/b%2$04d/model/w%1$04db%2$04d.mdl",
                    2)
        );
        MODEL_HELPERS.put(  // Off hand
            1, new ModelHelper(
                    "chara/weapon/w%1$04d/obj/body/b%2$04d/b%2$04d.imc",
                    0,
                    "chara/weapon/w%1$04d/obj/body/b%2$04d/model/w%1$04db%2$04d.mdl",
                    2)
        );
        MODEL_HELPERS.put(  // Head
            2, new ModelHelper(
                    "chara/equipment/e%1$04d/e%1$04d.imc",
                    0,
                    "chara/equipment/e%1$04d/model/c%5$04de%1$04d_met.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Body
            3, new ModelHelper(
                    "chara/equipment/e%1$04d/e%1$04d.imc",
                    1,
                    "chara/equipment/e%1$04d/model/c%5$04de%1$04d_top.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Hands
            4, new ModelHelper(
                    "chara/equipment/e%1$04d/e%1$04d.imc",
                    2,
                    "chara/equipment/e%1$04d/model/c%5$04de%1$04d_glv.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Waist
            5, null
        );
        MODEL_HELPERS.put(  // Legs
            6, new ModelHelper(
                    "chara/equipment/e%1$04d/e%1$04d.imc",
                    3,
                    "chara/equipment/e%1$04d/model/c%5$04de%1$04d_dwn.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Feet
            7, new ModelHelper(
                    "chara/equipment/e%1$04d/e%1$04d.imc",
                    4,
                    "chara/equipment/e%1$04d/model/c%5$04de%1$04d_sho.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Ears
            8, new ModelHelper(
                    "chara/accessory/a%1$04d/a%1$04d.imc",
                    0,
                    "chara/accessory/a%1$04d/model/c%5$04da%1$04d_ear.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Neck
            9, new ModelHelper(
                    "chara/accessory/a%1$04d/a%1$04d.imc",
                    1,
                    "chara/accessory/a%1$04d/model/c%5$04da%1$04d_nek.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Wrists
            10, new ModelHelper(
                    "chara/accessory/a%1$04d/a%1$04d.imc",
                    2,
                    "chara/accessory/a%1$04d/model/c%5$04da%1$04d_wrs.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // R.Ring
            11, new ModelHelper(
                    "chara/accessory/a%1$04d/a%1$04d.imc",
                    3,
                    "chara/accessory/a%1$04d/model/c%5$04da%1$04d_rir.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // L.Ring
            12, new ModelHelper(
                    "chara/accessory/a%1$04d/a%1$04d.imc",
                    4,
                    "chara/accessory/a%1$04d/model/c%5$04da%1$04d_ril.mdl",
                    1)
        );
        MODEL_HELPERS.put(  // Soul Crystal
            13, null
        );
    }

    /**
     * Character type fallbacks in case the requested one does not exist.
     */
    private static final Map<Integer, Integer> CHARACTER_TYPE_FALLBACK = new HashMap<>();
    static {
        CHARACTER_TYPE_FALLBACK.put(201, 101);
        CHARACTER_TYPE_FALLBACK.put(301, 101);
        CHARACTER_TYPE_FALLBACK.put(401, 201);
        CHARACTER_TYPE_FALLBACK.put(501, 101);
        CHARACTER_TYPE_FALLBACK.put(601, 201);
        CHARACTER_TYPE_FALLBACK.put(701, 101);
        CHARACTER_TYPE_FALLBACK.put(801, 201);
        CHARACTER_TYPE_FALLBACK.put(901, 101);
        CHARACTER_TYPE_FALLBACK.put(1001, 201);
        CHARACTER_TYPE_FALLBACK.put(1101, 101);
        CHARACTER_TYPE_FALLBACK.put(1201, 201);
        CHARACTER_TYPE_FALLBACK.put(1301, 101);
        CHARACTER_TYPE_FALLBACK.put(1401, 201);
    }

    // for EquipSlot
    public static String getModelKey(int slot, XivQuad key, int characterType) {
        ModelHelper helper = MODEL_HELPERS.get(slot);
        if (helper == null) {
            return null;
        }

        return String.format(helper.modelFileFormat, key.x, key.y, key.z, key.w, characterType);
    }

    public static Pair<ModelDefinition, ImcVariant> getModel(int slot, PackCollection packs, XivQuad key, int characterType) {
        ImcVariant variant;
        ModelHelper helper = MODEL_HELPERS.get(slot);
        if (helper == null) {
            return null;
        }

        int variantIndex = (slot >> (helper.variantIndexWord * 16)) & 0xFFFF;
        String imcPath = String.format(helper.imcFileFormat, key.x, key.y, key.z, key.w, characterType);
        PackFile imcBase = packs.tryGetFile(imcPath);
        if (imcBase == null) {
            return null;
        }

        ImcFile imc = new ImcFile(imcBase);
        variant = imc.getVariant((byte) helper.imcPartKey, variantIndex);

        String modelPath = String.format(helper.modelFileFormat, key.x, key.y, key.z, key.w, characterType);
        PackFile modelBase = packs.tryGetFile(modelPath);

        if (modelBase == null) {
            if (!CHARACTER_TYPE_FALLBACK.containsKey(characterType)) {
                return null;
            }

            // Character type fallbacks in case the requested one does not exist.
            characterType = CHARACTER_TYPE_FALLBACK.get(characterType);
            modelPath = String.format(helper.modelFileFormat, key.x, key.y, key.z, key.w, characterType);
            modelBase = packs.tryGetFile(modelPath);

            if (modelBase == null) {
                return null;
            }
        }

        ModelFile modelFile = (ModelFile) modelBase;
        ModelDefinition model = modelFile.getModelDefinition();

        return new Pair<>(model, variant);
    }

    // for monster model
    public static Pair<ModelDefinition, ImcVariant> getModelDefinition(ModelChara modelChara) {
        switch (modelChara.getType()) {
            case 3:
                return getMonsterModelDefinition(modelChara);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Pair<ModelDefinition, ImcVariant> getMonsterModelDefinition(ModelChara modelChara) {
        final String ImcPathFormat = "chara/monster/m%1$04d/obj/body/b%2$04d/b%2$04d.imc";
        final String ModelPathFormat = "chara/monster/m%1$04d/obj/body/b%2$04d/model/m%1$04db%2$04d.mdl";

        String imcPath = String.format(ImcPathFormat, modelChara.getModelKey(), modelChara.getBaseKey());
        String mdlPath = String.format(ModelPathFormat, modelChara.getModelKey(), modelChara.getBaseKey());

        PackCollection packs = modelChara.getSheet().getCollection().getPackCollection();

        log.info("imcPath:{}, mdlPath:{}", imcPath, mdlPath);

        PackFile imcFileBase = packs.tryGetFile(imcPath);
        PackFile mdlFileBase = packs.tryGetFile(mdlPath);

        if (imcFileBase == null || mdlFileBase == null) {
            throw new IllegalArgumentException("Unable to find files for ModelChara#" + modelChara.getKey());
        }

        try {
            ModelDefinition model = ((ModelFile)mdlFileBase).getModelDefinition();
            ImcFile imcFile = new ImcFile(imcFileBase);
            ImcVariant variant = imcFile.getVariant(modelChara.getVariant());

            return new Pair<>(model, variant);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to load model for {this}.", ex);
        }
    }
}
