package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;
import ffxiv.housim.saintcoinach.xiv.XivSheetName;

/**
 * Class representing parameters from the game data.
 */
@XivSheetName("BaseParam")
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

    @Override
    public String toString() {
        return getName();
    }

    // TODO Define EquipSlotCategory
    // TODO public int getMaximum(EquipSlotCategory category)

    /**
     * Get the value modifier of the current {@link BaseParam} for a certain role.
     * @param role Role to get the modifier for.
     * @return Returns the modifier for <code>role</code>, in percent.
     */
    public int getModifier(int role) {
        int Offset = 25;
        int Maximum = 12;
        if (role < 0 || role > Maximum) {
            return 0;
        }
        return (int) get(Offset + role);
    }
}