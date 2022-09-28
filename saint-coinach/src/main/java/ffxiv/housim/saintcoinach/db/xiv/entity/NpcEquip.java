package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class NpcEquip extends XivRow {
    public NpcEquip(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }
    
    public int getMainHandModel() {
        return asInt32("Model{MainHand}");
    }
    
    public Stain getMainHandDye() {
        return as(Stain.class,"Dye{MainHand}");
    }

    public int getOffHandModel() {
        return asInt32("Model{OffHand}");
    }

    public Stain getOffHandDye() {
        return as(Stain.class, "Dye{OffHand}");
    }
    
    public int getHeadModel() {
        return asInt32("Model{Head}");
    }

    public Stain getHeadDye() {
        return as(Stain.class, "Dye{Head}");
    }

    public boolean isVisor() {// FIXME
        return asBoolean("Visor");
    }

    public int getBodyModel() {
        return asInt32("Model{Body}");
    }

    public Stain getBodyDye() {
        return as(Stain.class, "Dye{Body}");
    }

    public int getHandsModel() {
        return asInt32("Model{Hands}");
    }

    public Stain getHandsDye() {
        return as(Stain.class, "Dye{Hands}");
    }

    public int getLegsModel() {
        return asInt32("Model{Legs}");
    }

    public Stain getLegsDye() {
        return as(Stain.class, "Dye{Legs}");
    }

    public int getFeetModel() {
        return asInt32("Model{Feet}");
    }

    public Stain getFeetDye() {
        return as(Stain.class, "Dye{Feet}");
    }

    public int getEarsModel() {
        return asInt32("Model{Ears}");
    }

    public Stain getEarsDye() {
        return as(Stain.class, "Dye{Ears}");
    }

    public int getNeckModel() {
        return asInt32("Model{Neck}");
    }

    public Stain getNeckDye() {
        return as(Stain.class, "Dye{Neck}");
    }

    public int getWristsModel() {
        return asInt32("Model{Wrists}");
    }

    public Stain getWristsDye() {
        return as(Stain.class, "Dye{Wrists}");
    }

    public int getLeftRingModel() {
        return asInt32("Model{LeftRing}");
    }

    public Stain getLeftRingDye() {
        return as(Stain.class, "Dye{LeftRing}");
    }

    public int getRightRingModel() {
        return asInt32("Model{RightRing}");
    }

    public Stain getRightRingDye() {
        return as(Stain.class, "Dye{RightRing}");
    }

}
