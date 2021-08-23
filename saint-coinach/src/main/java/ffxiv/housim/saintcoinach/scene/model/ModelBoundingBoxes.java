package ffxiv.housim.saintcoinach.scene.model;

import ffxiv.housim.saintcoinach.math.BoundingBox;

import java.nio.ByteBuffer;

public class ModelBoundingBoxes { // 9 in hsl
    // TODO: Purpose of having 4 boxes (or 8 vectors) is unknown.
    public BoundingBox value1;
    public BoundingBox value2;
    public BoundingBox value3;
    public BoundingBox value4;

    public ModelBoundingBoxes(ByteBuffer buffer) {
        value1 = new BoundingBox(buffer);
        value2 = new BoundingBox(buffer);
        value3 = new BoundingBox(buffer);
        value4 = new BoundingBox(buffer);
    }
}
