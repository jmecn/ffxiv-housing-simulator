package ffxiv.housim.app.core.indoor;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class Furniture {

    private Long id;

    ////////// Unite

    private Long uniteId;

    ///////// Furniture Item
    private Integer furnitureId;// -> HousingFurniture#Key

    private Integer furnitureItemId;// -> Item#Key

    //////// Transform

    private Vector3f position = new Vector3f(0f, 0f, 0f);

    private float yAngle = 0.0f;

    //////// Dye

    private boolean isDyeable = false;

    private Integer stainId = null;// -> Stain#Key

    private Integer stainItemId = null;// -> Item#Key

    private ColorRGBA stainColor = null;

    public Furniture(Long id, Integer furnitureId) {
        this.id = id;
        this.furnitureId = furnitureId;
    }

}