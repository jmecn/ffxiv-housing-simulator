package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.db.xiv.XivName;

/**
 * Class representing parameters from the game data.
 */
@XivName("BaseParam")
public class BaseParam extends XivRow {

    public BaseParam(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    /**
     * Gets the name of the parameter.
     *
     * @return The name of the parameter.
     */
    public String getName() {
        return asString("Name");
    }

    /**
     * Gets the description of the parameter.
     * <p>Not all parameters have a description.</p>
     *
     * @return The description of the parameter.
     */
    public String getDescription() {
        return asString("Description");
    }

    /**
     * Get the maximum value of the current {@link BaseParam} for a specific {@link EquipSlotCategory}.
     * @param category {@link EquipSlotCategory} to get the maximum parameter value for.
     * @return The maximum value for the current {@link BaseParam} on <code>category</code>.
     */
    public int getMaximum(EquipSlotCategory category) {
        int offset = 3;
        return category.getKey() == 0 ? 0 : (int) get(offset + category.getKey());
    }

    /**
     * Get the value modifier of the current {@link BaseParam} for a certain role.
     * @param role Role to get the modifier for.
     * @return Returns the modifier for <code>role</code>, in percent.
     */
    public int getModifier(int role) {
        int offset = 25;
        int maximum = 12;
        if (role < 0 || role > maximum) {
            return 0;
        }
        return (int) get(offset + role);
    }

    @Override
    public String toString() {
        return getName();
    }
}