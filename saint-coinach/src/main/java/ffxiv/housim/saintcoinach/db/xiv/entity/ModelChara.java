package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.material.imc.ImcFile;
import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.scene.model.ModelFile;
import ffxiv.housim.saintcoinach.utils.Pair;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class ModelChara extends XivRow {
    public ModelChara(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getType() {
        return asInt32("Type");
    }

    public int getModelKey() {
        return asInt32("Model");
    }

    public int getBaseKey() {
        return asInt32("Base");
    }

    public int getVariant() {
        return asInt32("Variant");
    }

    public Pair<ModelDefinition, ImcVariant> GetModelDefinition() {
        switch (getType()) {
            case 3:
                return GetMonsterModelDefinition();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    private Pair<ModelDefinition, ImcVariant> GetMonsterModelDefinition() {
        final String ImcPathFormat = "chara/monster/m%1$04d/obj/body/b%2$04d/b%2$04d.imc";
        final String ModelPathFormat = "chara/monster/m%1$04d/obj/body/b%2$04d/model/m%1$04db%2$04d.mdl";

        String imcPath = String.format(ImcPathFormat, getModelKey(), getBaseKey());
        String mdlPath = String.format(ModelPathFormat, getModelKey(), getBaseKey());

        PackCollection packs = getSheet().getCollection().getPackCollection();

        PackFile imcFileBase = packs.tryGetFile(imcPath);
        PackFile mdlFileBase = packs.tryGetFile(mdlPath);

        if (imcFileBase == null || mdlFileBase == null || !(mdlFileBase instanceof ModelFile)) {
            throw new IllegalArgumentException("Unable to find files for {this}.");
        }

        try {
            ModelDefinition model = ((ModelFile)mdlFileBase).getModelDefinition();
            ImcFile imcFile = new ImcFile(imcFileBase);
            ImcVariant variant = imcFile.getVariant(getVariant());

            return new Pair<>(model, variant);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to load model for {this}.", ex);
        }
    }
}
