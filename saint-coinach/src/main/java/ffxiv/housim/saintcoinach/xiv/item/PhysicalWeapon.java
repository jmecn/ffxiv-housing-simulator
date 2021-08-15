package ffxiv.housim.saintcoinach.xiv.item;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;

public class PhysicalWeapon extends Weapon {
    public PhysicalWeapon(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
