package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.entity.level.PlaceName;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class HousingExterior extends XivRow {

    // map
    public final static String s1h0 = "s1h0";// /sea_s1
    public final static String f1h0 = "f1h0";// /fst_f1
    public final static String w1h0 = "w1h0";// /wil_w1
    public final static String e1h0 = "e1h0";// /est_e1
    public final static String r1h0 = "r1h0";// /roc_r1

    // housing size
    // 254 -> _co -> common
    // 0 -> _s -> small
    // 1 -> _m -> medium
    // 2 -> _l -> large

    // /s_wal /s_rof
    // /m_wal /m_rof
    // /l_wal /l_rof

    // 1 bgcommon/hou/dyna/%s/%s_rof/%04d/asset/%s_%s_rof%04d.sgb -> bg/ffxiv/sea_s1/hou/dyna/s_rof/0001/asset/s1h0_s_rof0001.sgb
    // 2 bgcommon/hou/dyna/%s/%s_wal/%04d/asset/%s_%s_wal%04d.sgb
    // 3 bgcommon/hou/dyna/%s/wid/%04d/asset/%s_%s_wid%04d.sgb
    // 4 bgcommon/hou/dyna/%s/dor/%04d/asset/%s_%s_dor%04d.sgb -> bgcommon/hou/dyna/s1h0/dor/0001/asset/s1h0_co_dor0001.sgb
    // 5 bgcommon/hou/dyna/opt/rf/%04d/asset/opt_rf_m%04d.sgb
    // 6 bgcommon/hou/dyna/opt/wl/%04d/asset/opt_wl_m%04d.sgb
    // 7 bgcommon/hou/dyna/opt/sg/%04d/asset/opt_sg_m%04d.sgb
    // bgcommon/hou/dyna/%s/c_fnc/%04d/asset/%s_f_fnc%04da.sgb

    public HousingExterior(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public int getExteriorId() {
        return asInt16("ExteriorId");
    }

    public short getHousingItemCategory() {
        return asInt16("HousingItemCategory");
    }

    public HousingItemCategory getCategory() {
        short val = getHousingItemCategory();
        return HousingItemCategory.of(val);
    }

    public PlaceName getPlaceName() {
        return as(PlaceName.class);
    }

    public short getHousingSize() {
        return asInt16("HousingSize");
    }

    public HousingSize getSize() {
        return HousingSize.of(getHousingSize());
    }

    public String getModel() {
        return asString("Model");
    }

    @Override
    public String toString() {
        return String.format("(#%d: id=%d, cat=%s, size=%s)", getKey(), getExteriorId(), getCategory(), getSize());
    }
}
