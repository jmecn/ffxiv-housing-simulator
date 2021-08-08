package ffxiv.housim.saintcoinach.ex.sheet;

import ffxiv.housim.saintcoinach.ex.Language;
import ffxiv.housim.saintcoinach.ex.row.IDataRow;

import java.nio.ByteBuffer;

public interface IDataSheet<T extends IDataRow> extends ISheet<T> {

    Language getLanguage();

    ByteBuffer getBuffer();
}