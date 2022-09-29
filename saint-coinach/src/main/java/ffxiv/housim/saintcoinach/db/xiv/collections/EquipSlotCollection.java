package ffxiv.housim.saintcoinach.db.xiv.collections;

import ffxiv.housim.saintcoinach.db.xiv.XivCollection;
import ffxiv.housim.saintcoinach.db.xiv.entity.EquipSlot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class EquipSlotCollection implements Iterable<EquipSlot> {

    private static final int SLOT_COUNT = 14;

    private List<EquipSlot> equipSlots;

    @Getter
    private XivCollection collection;
    public EquipSlotCollection(XivCollection collection) {
        this.collection = collection;
        equipSlots = new ArrayList<>(SLOT_COUNT);
        for (int i = 0; i < SLOT_COUNT; i++) {
            EquipSlot equipSlot = new EquipSlot(this, i);
            equipSlots.add(equipSlot);
        }
    }

    public EquipSlot get(int key) {
        return equipSlots.get(key);
    }

    @Override
    public Iterator<EquipSlot> iterator() {
        return equipSlots.iterator();
    }
}
