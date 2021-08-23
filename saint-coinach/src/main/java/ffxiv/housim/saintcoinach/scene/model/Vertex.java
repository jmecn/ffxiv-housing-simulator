package ffxiv.housim.saintcoinach.scene.model;

import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector4;

public final class Vertex {
    public Vector4 position;
    public Vector4 blendWeights;
    public int blendIndices;
    public Vector3 normal;

    // TODO: For dual textures it's two components per texture, unknown purpose of Z and W components on others.
    public Vector4 uv;
    public Vector4 color;
    public Vector4 tangent2;
    public Vector4 tangent1;
}
