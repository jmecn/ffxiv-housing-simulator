package ffxiv.housim.saintcoinach.utils;

import ffxiv.housim.saintcoinach.db.xiv.entity.ModelChara;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.material.imc.ImcFile;
import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import lombok.extern.slf4j.Slf4j;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
@Slf4j
public final class ModelHelper {

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

        if (imcFileBase == null || mdlFileBase == null || !(mdlFileBase instanceof ModelFile)) {
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
