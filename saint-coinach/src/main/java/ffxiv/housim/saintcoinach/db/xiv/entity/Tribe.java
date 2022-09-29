package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class Tribe extends XivRow {
    // XXX: Magic happening here
    static final short[][] MODEL_KEYS = new short[][]{
            {101, 202}, // 1 - Hyur Midlander
            {301, 401}, // 2 - Hyur Highlander
            {501, 601}, // 3 - Elezen Wildwood
            {501, 601}, // 4 - Elezen Duskwight
            {1101, 1201}, // 5 - Lalafell Plainsfolk
            {1101, 1201}, // 6 - Lalafell Dunesfolk
            {701, 801}, // 7 - Miqo'te Seeker of the Sun
            {701, 801}, // 8 - Miqo'te Keeper of the Moon
            {901, 1001}, // 9 - Roegadyn Sea Wolf
            {901, 1001}, // 10 - Roegadyn Hellsguard
            {1301, 1401}, // 11 - Au Ra Raen
            {1301, 1401}, // 12 - Au Ra Xaela
    };

    public Tribe(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public short getMaleModelTypeKey() {
        return MODEL_KEYS[this.getKey() - 1][0];
    }

    public short getFemaleModelTypeKey() {
        return MODEL_KEYS[this.getKey() - 1][1];
    }

    public String getMasculine() {
        return asString("Masculine");
    }
    public String getFeminine() {
        return asString("Feminine");
    }
    public int getStrengthBonus() {
        return asInt32("STR");
    }
    public int getVitalityBonus() {
        return asInt32("VIT");
    }
    public int getDexterityBonus() {
        return asInt32("DEX");
    }
    public int getIntelligenceBonus() {
        return asInt32("INT");
    }
    public int getMindBonus() {
        return asInt32("MND");
    }
    public int getPietyBonus() {
        return asInt32("PIE");
    }

    @Override
    public String toString() {
        return getFeminine();
    }
}
