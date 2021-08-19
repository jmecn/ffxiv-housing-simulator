package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.graphics.ModelFile;
import lombok.Getter;

public class ModelDefinition {
    @Getter
    private String[] boneNames;

    public ModelDefinition(ModelFile modelFile) {
        // TODO
    }

    public Model getModel(ModelQuality high) {
        // TODO
        return new Model();
    }
}
