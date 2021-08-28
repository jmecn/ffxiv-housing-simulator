package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

public enum HousingItemCategory {


    ROOF(1, "房顶"),// rof
    EXTERIOR_WALL(2, "外墙"),//
    WINDOW(3, "窗户"),// wid
    DOOR(4, "门"),// dor

    RF(5, "烟囱"),// rf bgcommon/hou/dyna/opt/rf/%04d/assert/opt_rf_m%04d.sgb 0~8
    SG(6, "门牌"),// sg bgcommon/hou/dyna/opt/sg/%04d/assert/opt_sg_m%04d.sgb 0~9
    WL(7, "遮篷"),// wl bgcommon/hou/dyna/opt/wl/%04d/assert/opt_wl_m%04d.sgb 0~6

    FENCE(8, "栅栏"),

    INTERIOR_WALL(9, "内墙"),
    FLOOR(10, "地板"),
    LIGHT(11, "屋顶照明"),

    FURNISHING(12, "家具"),
    TABLE(13, "桌台"),
    TABLE_TOP(14, "桌上"),
    WALL_MOUNTED(15, "壁挂"),
    Rug(16, "地毯"),
    OUTDOOR_FURNISHING(17, "庭具"),

    // 18 cho bgcommon/hou/outdoor/cho/%04d/asset/cho_f%d_m%04d.sgb
    // 19 frm bgcommon/hou/outdoor/frm/%04d/asset/frm_f%d_m%04d.sgb
    ;

    int value;
    String desc;

    HousingItemCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
