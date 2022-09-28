package ffxiv.housim.saintcoinach.db.xiv.music.bgm;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class BGMSituation extends XivRow {
    public BGMSituation(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public BGM getDaytime() {
        return as(BGM.class, "DaytimeID");
    }

    public BGM getNight() {
        return as(BGM.class, "NightID");
    }

    public BGM getBattle() {
        return as(BGM.class, "BattleID");
    }

    public BGM getDaybreak() {
        return as(BGM.class, "DaybreakID");
    }

    public BGM getTwilight() {
        return as(BGM.class, "TwilightID");
    }
}
