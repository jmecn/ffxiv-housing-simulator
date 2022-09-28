package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;
import ffxiv.housim.saintcoinach.texture.ImageFile;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class Emote extends XivRow {
    public Emote(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public EmoteCategory getEmoteCategory() {
        return as(EmoteCategory.class);
    }

    public ImageFile getIcon() {
        return asImage("Icon");
    }

    public LogMessage getTargetedLogMessage() {
        return as(LogMessage.class, "LogMessage{Targeted}");
    }

    public LogMessage getUntargetedLogMessage() {
        return as(LogMessage.class, "LogMessage{Untargeted}");
    }

    @Override
    public String toString() {
        return getName();
    }
}
