package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import java.util.HashMap;
import java.util.Map;

public enum HousingItemCategory {

    ROF(1, "房顶", "bgcommon/hou/dyna/%s/%s_rof/%04d/asset/%s_%s_rof%04d.sgb", "bg/ffxiv/%s/hou/dyna/%s_rof/%04d/asset/%s_%s_rof%04d.sgb"),
    WAL(2, "外墙", "bgcommon/hou/dyna/%s/%s_wal/%04d/asset/%s_%s_wal%04d.sgb", "bg/ffxiv/%s/hou/dyna/%s_wal/%04d/asset/%s_%s_wal%04d.sgb"),
    WID(3, "窗户", "bgcommon/hou/dyna/%s/wid/%04d/asset/%s_%s_wid%04d.sgb"),
    DOR(4, "房门", "bgcommon/hou/dyna/%s/dor/%04d/asset/%s_%s_dor%04d.sgb"),

    RF(5, "烟囱", "bgcommon/hou/dyna/opt/rf/%04d/asset/opt_rf_m%04d.sgb"),
    WL(6, "遮篷", "bgcommon/hou/dyna/opt/wl/%04d/asset/opt_wl_m%04d.sgb"),
    SG(7, "门牌", "bgcommon/hou/dyna/opt/sg/%04d/asset/opt_sg_m%04d.sgb"),

    FNC(8, "栅栏", "bg/ffxiv/%s/hou/dyna/c_fnc/%04d/asset/%s_f_fnc%04da.sgb"),

    ROM_WL(9, "内墙", "bgcommon/hou/dyna/mat/wl/%04d/material/rom_wl_2%04da.mtrl"),
    ROM_FL(10, "地板", "bgcommon/hou/dyna/mat/fl/%04d/material/rom_fl_2%04da.mtrl"),
    LMP(11, "屋顶照明", "bgcommon/hou/dyna/lmp/lp/%04d/asset/lmp_s0_m%04d.sgb"),

    FURNISHING(12, "家具", "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb"),
    TABLE(13, "桌台", "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb"),
    TABLE_TOP(14, "桌上", "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb"),
    WALL_MOUNTED(15, "壁挂", "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb"),
    Rug(16, "地毯", "bgcommon/hou/indoor/general/%04d/asset/fun_b0_m%04d.sgb"),

    GAR(17, "庭具", "bgcommon/hou/outdoor/general/%04d/asset/gar_b0_m%04d.sgb"),

    // 18 cho bgcommon/hou/outdoor/cho/%04d/asset/cho_f%d_m%04d.sgb
    // 19 frm bgcommon/hou/outdoor/frm/%04d/asset/frm_f%d_m%04d.sgb
    ;

    short value;
    String name;
    String[] format;

    HousingItemCategory(int value, String name, String ... format) {
        this.value = (short) value;
        this.name = name;
        this.format = format;
    }

    public short getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getFormat(int index) {
        return format[index];
    }

    // ui/uld/
    // h_ui_rot02_o
    // h_ui_trn02_o
    // vfx/common/eff/%s.avfx

    final static Map<Short, HousingItemCategory> CACHE = new HashMap<>();
    static {
        for(HousingItemCategory e : values()) {
            CACHE.put(e.value, e);
        }
    }
    public static HousingItemCategory of (short val) {
        return CACHE.get(val);
    }
}
