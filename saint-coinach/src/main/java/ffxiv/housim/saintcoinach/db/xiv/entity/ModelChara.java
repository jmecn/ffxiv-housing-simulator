package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * Class representing model data (non-humanoid).
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class ModelChara extends XivRow {
    public ModelChara(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    /**
     * Gets the type of the current model.
     *
     * <pre>
     * Confirmed values are:
     *     2: Demihuman (chara/demihuman/d{Model}/obj/equipment/e{Base}/model/d{Model}e{Base}_{Variant}.mdl)
     *         -> TODO: Variant to str map
     *     3: Monster (chara/monster/m{Model}/base/b{Base}/model/m{Model}b{Base}.mdl)
     *     4: Static object (?)
     *     5: Attached to other NPC, Golem Soulstone, Titan Heart, etc.
     * Unconfirmed:
     *     1: Special body type? Gaius, Nero, Rhitatyn have this
     * </pre>
     * @return The type of the current model.
     */
    public short getType() {
        return asInt16("Type");
    }

    /**
     * Gets the key for the current model's file.
     * @return The key for the current model's file.
     */
    public short getModelKey() {
        return asInt16("Model");
    }

    /**
     * Gets the key for the base of the model to use.
     * @return The key for the base of the model to use.
     */
    public short getBaseKey() {
        return asInt16("Base");
    }

    /**
     * Gets the variant of the model to use.
     * @return The variant of the model to use.
     */
    public short getVariant() {
        return asInt16("Variant");
    }
}
