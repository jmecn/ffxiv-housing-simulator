package ffxiv.housim.saintcoinach.scene.mesh;

import ffxiv.housim.saintcoinach.scene.model.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

public class MeshPart {

    public Mesh mesh;
    public MeshPartHeader header;
    public int indexOffset;
    public int indexCount;

    public ModelAttribute[] attributes;

    MeshPart(Mesh mesh, MeshPartHeader header, byte[] indexBuffer) {
        this.mesh = mesh;
        this.header = header;
        this.indexOffset = header.indexOffset;
        this.indexCount = header.indexCount;

        List<ModelAttribute> attr = new ArrayList<>();

        ModelAttribute[] attributes = mesh.getModel().getDefinition().getAttributes();
        for (int i = 0; i < attributes.length; ++i) {
            if(((header.attributesMask >> i) & 1) == 1) {
                attr.add(attributes[i]);
            }
        }

        this.attributes = attr.toArray(new ModelAttribute[0]);
    }
}
