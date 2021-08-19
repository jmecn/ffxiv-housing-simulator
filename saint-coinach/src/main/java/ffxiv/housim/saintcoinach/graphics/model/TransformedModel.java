package ffxiv.housim.saintcoinach.graphics.model;

import ffxiv.housim.saintcoinach.math.Vector3;
import lombok.Getter;

public class TransformedModel {
    @Getter
    private final ModelDefinition model;
    @Getter
    private final Vector3 translation;
    @Getter
    private final Vector3 rotation;
    @Getter
    private final Vector3 scale;

    public TransformedModel(ModelDefinition model, Vector3 translation, Vector3 rotation, Vector3 scale) {
        this.model = model;
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }
}
