package ffxiv.housim.saintcoinach.db.xiv.item;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;

public class PhysicalWeapon extends Weapon {
    public PhysicalWeapon(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
}
