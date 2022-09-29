package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing equipment slot categories from the game data.
 */
public class EquipSlotCategory extends XivRow {
    /**
     * {@link EquipSlot}s that get blocked when using the current {@link EquipSlotCategory}
     */
    @Getter
    private List<EquipSlot> blockedSlots;
    /**
     * {@link EquipSlot}s on which the current {@link EquipSlotCategory} can be used.
     */
    @Getter
    private List<EquipSlot> possibleSlots;

    public EquipSlotCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
        build();
    }

    /**
     * Build the list of blocked and possible {@link EquipSlot}s for the current {@link EquipSlotCategory}.
     * <pre>
     * Columns of the EquipSlotCategory are used as keys for {@link EquipSlot}s.
     * A column value of -1 means that the {@link EquipSlot} gets blocked and a value of 1 is used for possible {@link EquipSlot}s.
     * </pre>
     */
    private void build() {
        List<EquipSlot> blocked = new ArrayList<>();
        List<EquipSlot> possible = new ArrayList<>();

        for (EquipSlot slot : getSheet().getCollection().getEquipSlots()) {
            int val = (int) get(slot.getKey());
            if (val > 0) {
                possible.add(slot);
            } else if (val < 0) {
                blocked.add(slot);
            }
        }

        blockedSlots = blocked;
        possibleSlots = possible;
    }
}
