package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

public enum HousingItemCategory {


    ROOF(1, "房顶"),
    EXTERIOR_WALL(2, "外墙"),
    WINDOW(3, "窗户"),
    DOOR(4, "门"),

    FENCE(8, "栅栏"),

    // 烟囱
    // 遮篷
    INTERIOR_WALL(9, "内墙"),
    FLOOR(10, "地板"),
    LIGHT(11, "屋顶照明"),

    FURNISHING(12, "家具"),
    TABLE(13, "桌台"),
    TABLE_TOP(14, "桌上"),
    WALL_MOUNTED(15, "壁挂"),
    Rug(16, "地毯"),
    OUTDOOR_FURNISHING(17, "庭具"),

    ;

    int value;
    String desc;

    HousingItemCategory(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
