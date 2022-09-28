package ffxiv.housim.saintcoinach.db.xiv.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public enum ActionCostType {// byte
    NoCost, // = 0
    HPPercent,
    Unknown2,
    MP,
    MPAll,
    TP,
    Unknown6,
    GP,
    CP,
    Unknown9,
    StatusAll,
    LimitBreakGauge,
    Unknown12,
    Unknown13,
    AstrologianCard,
    Unknown15,
    StatusAmount,
    Unknown17,
    TPAll,
    Unknown19,
    Unknown20, // Contents Actions?
    Unknown21,
    BeastGauge,
    Polyglot,
    DreadwyrmAether,
    BloodGauge,
    Unknown26,
    NinkiGauge,
    Chakra,
    GreasedLightning,
    Aetherflow,
    AethertrailAttunement,
    Repertoire,
    AstrologianCard2,
    AstrologianCard3,
    AstrologianCard4,
    AstrologianCard5,
    AstrologianCard6,
    Unknown38,
    KenkiGauge,
    Unknown40,
    OathGauge,
    Unknown42,
    BalanceGauge,
    Unknown44,
    KenkiGauge2,
    FaerieGauge,
    DreadwyrmTrance,
    UnknownDragoon48,
    LilyAll,
    AstralFireOrUmbralIce,
    MP2,
    Ceruleum,
    FourfoldFeather,
    Espirit,
    Cartridge,
    BloodLily,
    Lily,
    SealsAll,
    SoulVoice,
    Unknown60,
    Heat,
    Battery,
    Meditation,
    Soul,
    Shroud,
    LemureShroud,
    VoidShroud,
    Addersgall,
    Addersting,
    Unknown70, // Don't know what's this. maybe a skill dismisser, used by Eukrasian Diagnosis
    Arcanum, // This is strange, mark this, 1 is ifrit, 2 is titan, 3 is garuda
    FireAttunement,
    EarthAttunement,
    WindAttunement,
    Firstmind,
    Paradox,
    Unknown77,
    ManaStack,
    BeastChakra,
    Unknown80,
    Coda,
    Enshrouded,
    Unknown83; // Don't know what's this for, used by Eukrasian Prognosis

    final static Map<Integer, ActionCostType> CACHE;
    static {
        CACHE = new HashMap<>();
        int i = 0;
        for(ActionCostType e : values()) {
            CACHE.put(i, e);
            i++;
        }
    }

    public static ActionCostType of(int value) {
        return CACHE.get(value);
    }
}
