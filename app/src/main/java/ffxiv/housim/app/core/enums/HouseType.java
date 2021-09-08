package ffxiv.housim.app.core.enums;

import lombok.Getter;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/8
 */
@Getter
public enum HouseType {

    Small(1, "house.type.small", false, true, 200, 6, 2, 6, 20, 3, 1),
    Medium(2, "house.type.medium", true, true, 300, 8, 3, 6, 30, 3, 2),
    Large(3, "house.type.large", true, true, 400, 10, 4, 10, 40, 3, 3),
    Room(4, "house.type.room", false, false, 100, 4, 2, 4);

    private int value;// 值
    private String desc;// 描述
    private boolean has2nd;// 是否有2楼
    private boolean hasBase;// 是否有地下室
    private int limitFurniture;// 家具位
    private int limitNPC;// 契约NPC
    private int limitPlant;// 盆摘
    private int limitFishTank;// 水族箱
    private int limitYardObject;// 庭具位
    private int limitTarget;// 木人
    private int limitField;// 田地

    HouseType(int value, String desc, boolean has2nd, boolean hasBase, int limitFurniture, int limitNPC
              , int limitPlant, int limitFishTank) {
        this.value = value;
        this.desc = desc;
        this.has2nd = has2nd;
        this.hasBase = hasBase;
        this.limitFurniture = limitFurniture;
        this.limitNPC = limitNPC;
        this.limitPlant = limitPlant;
        this.limitFishTank = limitFishTank;
        this.limitYardObject = 0;
        this.limitTarget = 0;
        this.limitField = 0;
    }

    HouseType(int value, String desc, boolean has2nd, boolean hasBase, int limitFurniture, int limitNPC
              , int limitPlant, int limitFishTank, int limitYardObject, int limitTarget, int limitField) {
        this.value = value;
        this.desc = desc;
        this.has2nd = has2nd;
        this.hasBase = hasBase;
        this.limitFurniture = limitFurniture;
        this.limitNPC = limitNPC;
        this.limitPlant = limitPlant;
        this.limitFishTank = limitFishTank;
        this.limitYardObject = limitYardObject;
        this.limitTarget = limitTarget;
        this.limitField = limitField;
    }


}