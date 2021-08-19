package ffxiv.housim.saintcoinach.graphics.model;

import java.util.HashMap;
import java.util.Map;

public class ModelAttribute {
    public final static Map<String, Integer> MaskMap;

    private ModelDefinition definition;
    private String name;
    private int index;
    private int attributeMask;

    public ModelAttribute(ModelDefinition definition, int index) {
        this.definition = definition;
        this.index = index;
        this.name = definition.attributeNames[index];
        this.attributeMask = MaskMap.getOrDefault(name, 0);
    }

    static {
        // TODO: Not actually confirmed that these are all correct.
        Map<String, Integer> m = new HashMap<>();
        // tv_* seen on equipment > top
        m.put("atr_tv_a", 1 << 0);
        m.put("atr_tv_b", 1 << 1);
        m.put("atr_tv_c", 1 << 2);
        m.put("atr_tv_d", 1 << 3);
        m.put("atr_tv_e", 1 << 4);
        m.put("atr_tv_f", 1 << 5);
        m.put("atr_tv_g", 1 << 6);
        m.put("atr_tv_h", 1 << 7);
        m.put("atr_tv_i", 1 << 8);
        m.put("atr_tv_j", 1 << 9);

        // mv_* seen on equipment > met
        m.put("atr_mv_a", 1 << 0);
        m.put("atr_mv_b", 1 << 1);
        m.put("atr_mv_c", 1 << 2);
        m.put("atr_mv_d", 1 << 3);
        m.put("atr_mv_e", 1 << 4);
        m.put("atr_mv_f", 1 << 5);
        m.put("atr_mv_g", 1 << 6);
        m.put("atr_mv_h", 1 << 7);
        m.put("atr_mv_i", 1 << 8);
        m.put("atr_mv_j", 1 << 9);

        // bv_* seen on * > body
        m.put("atr_bv_a", 1 << 0);
        m.put("atr_bv_b", 1 << 1);
        m.put("atr_bv_c", 1 << 2);
        m.put("atr_bv_d", 1 << 3);
        m.put("atr_bv_e", 1 << 4);
        m.put("atr_bv_f", 1 << 5);
        m.put("atr_bv_g", 1 << 6);
        m.put("atr_bv_h", 1 << 7);
        m.put("atr_bv_i", 1 << 8);
        m.put("atr_bv_j", 1 << 9);

        // gv_* (probably) on equipment > glv
        m.put("atr_gv_a", 1 << 0);
        m.put("atr_gv_b", 1 << 1);
        m.put("atr_gv_c", 1 << 2);
        m.put("atr_gv_d", 1 << 3);
        m.put("atr_gv_e", 1 << 4);
        m.put("atr_gv_f", 1 << 5);
        m.put("atr_gv_g", 1 << 6);
        m.put("atr_gv_h", 1 << 7);
        m.put("atr_gv_i", 1 << 8);
        m.put("atr_gv_j", 1 << 9);

        // dv_* (probably) on equipment > dwn
        m.put("atr_dv_a", 1 << 0);
        m.put("atr_dv_b", 1 << 1);
        m.put("atr_dv_c", 1 << 2);
        m.put("atr_dv_d", 1 << 3);
        m.put("atr_dv_e", 1 << 4);
        m.put("atr_dv_f", 1 << 5);
        m.put("atr_dv_g", 1 << 6);
        m.put("atr_dv_h", 1 << 7);
        m.put("atr_dv_i", 1 << 8);
        m.put("atr_dv_j", 1 << 9);

        // sv_* (probably) on equipment > sho
        m.put("atr_sv_a", 1 << 0);
        m.put("atr_sv_b", 1 << 1);
        m.put("atr_sv_c", 1 << 2);
        m.put("atr_sv_d", 1 << 3);
        m.put("atr_sv_e", 1 << 4);
        m.put("atr_sv_f", 1 << 5);
        m.put("atr_sv_g", 1 << 6);
        m.put("atr_sv_h", 1 << 7);
        m.put("atr_sv_i", 1 << 8);
        m.put("atr_sv_j", 1 << 9);

        MaskMap = Map.copyOf(m);
    }

}