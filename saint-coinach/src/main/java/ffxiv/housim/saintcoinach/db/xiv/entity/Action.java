package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class Action extends ClassJobActionBase {
    public Action(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public ActionCategory getActionCategory() {
        return as(ActionCategory.class);
    }

    public int getSpellGroup() {
        return asInt32("SpellGroup");
    }

    public int getRange() {
        return asInt32("Range");
    }

    public boolean isRoleAction() {
        return asBoolean("IsRoleAction");
    }

    public boolean canTargetSelf() {
        return asBoolean("CanTargetSelf");
    }

    public boolean canTargetParty() {
        return asBoolean("CanTargetParty");
    }

    public boolean canTargetFriendly() {
        return asBoolean("CanTargetFriendly");
    }

    public boolean canTargetHostile() {
        return asBoolean("CanTargetHostile");
    }

    public boolean canTargetDead() {
        return asBoolean("CanTargetDead");
    }

    public boolean targetArea() {
        return asBoolean("TargetArea");
    }

    public int getEffectRange() {
        return asInt32("EffectRange");
    }

    public Action getComboFrom() {
        return  as(Action.class, "Action{Combo}");
    }

    public Status getGainedStatus() {
        return as(Status.class, "Status{GainSelf}");
    }

    public ActionCostType getCostType()  {
        return ActionCostType.of(asInt16("PrimaryCost{Type}"));
    }

    public int getCost() {
        return asInt16("PrimaryCost{Value}");
    }

    public int getCastTime() {
        return 100 * asInt16("Cast<100ms>");
    }

    public int getRecastTime() {
        return 100 * asInt16("Recast<100ms>");
    }

    public int getMpCost(int level) {
        IXivSheet<ParamGrow> paramGrowSheet = getSheet().getCollection().getSheet(ParamGrow.class);
        if (!paramGrowSheet.containsRow(level))
            return 0;
        ParamGrow paramGrow = paramGrowSheet.get(level);

        return (int)(paramGrow.getMpModifier() * getCost());
    }
}
