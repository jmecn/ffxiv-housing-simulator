package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.xiv.collections.EquipSlotCollection;
import ffxiv.housim.saintcoinach.material.imc.ImcVariant;
import ffxiv.housim.saintcoinach.math.XivQuad;
import ffxiv.housim.saintcoinach.scene.model.ModelDefinition;
import ffxiv.housim.saintcoinach.utils.ModelHelper;
import ffxiv.housim.saintcoinach.utils.Pair;
import lombok.Getter;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class EquipSlot {
    // Row key offset in the <c>Addon</c> sheet to get an equipment slot's name from.
    // This might change in updates, you never know with SE.
    private static final int ADDON_KEY_OFFSET = 738;

    /**
     * The keys are based on the columns in {@link EquipSlotCategory}
     */
    @Getter
    private final int key;

    @Getter
    private final EquipSlotCollection collection;

    public EquipSlot(EquipSlotCollection collection, int key) {
        this.collection = collection;
        this.key = key;
    }

    public String getName() {
        return collection.getCollection().getSheet(Addon.class).get(key + ADDON_KEY_OFFSET).getText();
    }

    public Pair<ModelDefinition, ImcVariant> getModel(XivQuad key, int characterType) {
        return ModelHelper.getModel(getKey(), collection.getCollection().getPackCollection(), key, characterType);
    }

    public String getModelKey(XivQuad key, int characterType) {
        return ModelHelper.getModelKey(getKey(), key, characterType);
    }

    @Override
    public String toString() {
        return getName();
    }
}
