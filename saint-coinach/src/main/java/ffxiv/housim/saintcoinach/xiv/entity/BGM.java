package ffxiv.housim.saintcoinach.xiv.entity;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.XivRow;

public class BGM extends XivRow {
    public BGM(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getFile() {
        return asString("File");
    }

    public byte getPriority() {
        return asByte("Priority");
    }

    public boolean isDisableRestartTimeOut() {
        return asBoolean("DisableRestartTimeOut");
    }

    public boolean isDisableRestart() {
        return asBoolean("DisableRestart");
    }

    public boolean isPassEnd() {
        return asBoolean("PassEnd");
    }

    public float getDisableRestartResetTime() {
        return asSingle("DisableRestartResetTime");
    }

    public byte getSpecialMode() {
        return asByte("SpecialMode");
    }
}
