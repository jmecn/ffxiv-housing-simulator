package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.math.XivQuad;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class ENpcBase extends XivRow {
    private static final int DATA_COUNT = 32;

    private List<IRelationalRow> assignedData;

    public ENpcBase(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public ModelChara getModelChara() {
        return as(ModelChara.class);
    }
    public Race getRace() {
        return as(Race.class);
    }
    public int getGender() {
        return asInt32("Gender");
    }
    public int getBodyType() {
        return asInt32("BodyType");
    }
    public int getHeight() {
        return asInt32("Height");
    }
    public Tribe getTribe() {
        return as(Tribe.class);
    }
    public int getFace() {
        return asInt32("Face");
    }
    public int getHairStyle() {
        return asInt32("HairStyle");
    }
    public int getHairHighlight() {
        return asInt32("HairHighlight");
    }
    public int getSkinColor() {
        return asInt32("SkinColor");
    }
    public int getEyeHeterochromia() {
        return asInt32("EyeHeterochromia");
    }
    public int getHairColor() {
        return asInt32("HairColor");
    }
    public int getHairHighlightColor() {
        return asInt32("HairHighlightColor");
    }
    public int getFacialFeature() {
        return asInt32("FacialFeature");
    }
    public int getFacialFeatureColor() {
        return asInt32("FacialFeatureColor");
    }
    public int getEyebrows() {
        return asInt32("Eyebrows");
    }
    public int getEyeColor() {
        return asInt32("EyeColor");
    }
    public int getEyeShape() {
        return asInt32("EyeShape");
    }
    public int getNose() {
        return asInt32("Nose");
    }
    public int getJaw() {
        return asInt32("Jaw");
    }
    public int getMouth() {
        return asInt32("Mouth");
    }
    public int getLipColor() {
        return asInt32("LipColor");
    }
    public int getBustOrTone1() {
        return asInt32("BustOrTone1");
    }

    public int getExtraFeature1() {
        return asInt32("ExtraFeature1");
    }
    public int getExtraFeature2OrBust() {
        return asInt32("ExtraFeature2OrBust");
    }
    public int getFacePaint() {
        return asInt32("FacePaint");
    }
    public int getFacePaintColor() {
        return asInt32("FacePaintColor");
    }
    public Behavior getBehavior() {
        return as(Behavior.class);
    }
    public XivQuad getModelMainHand() {
        return asQuad("Model{MainHand}");
    }
    public Stain getDyeMainHand() {
        return as(Stain.class, "Dye{MainHand}");
    }
    public XivQuad getModelOffHand() {
        return asQuad("Model{OffHand}");
    }
    public Stain getDyeOffHand() {
        return as(Stain.class, "Dye{OffHand}");
    }
    public int[] getModelHead() {
        return asIntArray("Model{Head}");
    }
    public int[] getModelBody() {
        return asIntArray("Model{Body}");
    }
    public int[] getModelHands() {
        return asIntArray("Model{Hands}");
    }
    public int[] getModelLegs() {
        return asIntArray("Model{Legs}");
    }
    public int[] getModelFeet() {
        return asIntArray("Model{Feet}");
    }
    public int[] getModelEars() {
        return asIntArray("Model{Ears}");
    }
    public int[] getModelNeck() {
        return asIntArray("Model{Neck}");
    }
    public int[] getModelWrists() {
        return asIntArray("Model{Wrists}");
    }
    public int[] getModelLeftRing() {
        return asIntArray("Model{LeftRing}");
    }
    public int[] getModelRightRing() {
        return asIntArray("Model{RightRing}");
    }
    public Stain getDyeHead() {
        return as(Stain.class, "Dye{Head}");
    }
    public Stain getDyeBody() {
        return as(Stain.class, "Dye{Body}");
    }
    public Stain getDyeHands() {
        return as(Stain.class, "Dye{Hands}");
    }
    public Stain getDyeLegs() {
        return as(Stain.class, "Dye{Legs}");
    }
    public Stain getDyeFeet() {
        return as(Stain.class, "Dye{Feet}");
    }
    public Stain getDyeEars() {
        return as(Stain.class, "Dye{Ears}");
    }
    public Stain getDyeNeck() {
        return as(Stain.class, "Dye{Neck}");
    }
    public Stain getDyeWrists() {
        return as(Stain.class, "Dye{Wrists}");
    }
    public Stain getDyeLeftRing() {
        return as(Stain.class, "Dye{LeftRing}");
    }
    public Stain getDyeRightRing() {
        return as(Stain.class, "Dye{RightRing}");
    }
    public NpcEquip getNpcEquip() {
        return as(NpcEquip.class);
    }

    public List<IRelationalRow> getAssignedData() {
        if (assignedData == null) {
            assignedData = buildAssignedData();
        }
        return assignedData;
    }

    public IRelationalRow getData(int index) {
        return as(IRelationalRow.class, "ENpcData", index);
    }
    public int getRawData(int index) {
        String fulCol = buildColumnName("ENpcData", index);
        Object raw = ((IRelationalRow)this).getRaw(fulCol);
        return (int) raw;
    }

    private List<IRelationalRow> buildAssignedData() {
        List<IRelationalRow> data = new ArrayList<>();

        for (int i = 0; i < DATA_COUNT; ++i) {
            IRelationalRow val = getData(i);
            if (val != null)
                data.add(val);
        }

        return data;
    }
}
