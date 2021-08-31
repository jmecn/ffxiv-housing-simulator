package ffxiv.housim.saintcoinach.scene.lgb;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/31
 */
public enum LgbEntryType {
    Unsupported(-1),
    None(0, 0x5C),// WAHT ?
    Model(1, 0x5C),// BgParts Keep this for backwards compatability
    WTF(2, 0x5C),// what ?
    Light(3, 0x6C),
    Vfx(4, 0x58),
    PositionMarker(5),
    Gimmick(6),// SharedGroup6 // secondary variable is set to 2
    Sound(7, 0xCC),
    EventNpc(8),
    BattleNpc(9),
    Aetheryte(12),
    EnvSpace(13),
    Gathering(14),
    SharedGroup15(15), // secondary variable is set to 13
    Treasure(16),
    Weapon(39),
    PopRange(40),
    ExitRange(41),
    MapRange(43),
    NaviMeshRange(44),
    EventObject(45),
    EnvLocation(47),
    EventRange(49),
    QuestMarker(51),
    CollisionBox(57, 0x58),
    DoorRange(58, 0x44),
    LineVfx(59),
    ClientPath(65),
    ServerPath(66),
    GimmickRange(67),
    TargetMarker(68, 0x38),
    ChairMarker(69, 0x38),
    ClickableRange(70),
    PrefetchRange(71),
    FateRange(72),
    SphereCastRange(75, 0x3C),
    ;
    final int value;
    final int size;

    LgbEntryType(int value) {
        this.value = value;
        this.size = -1;
    }

    LgbEntryType(int value, int size) {
        this.value = value;
        this.size = size;
    }

    static Map<Integer, LgbEntryType> CACHE = new HashMap<>();
    static {
        for (LgbEntryType e : values()) {
            CACHE.put(e.value, e);
        }
    }

    public static LgbEntryType of(int value) {
        return CACHE.getOrDefault(value, Unsupported);
    }
}
