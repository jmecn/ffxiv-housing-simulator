package ffxiv.housim.saintcoinach.db.ex;

import java.nio.ByteBuffer;

public interface IDataSheet<T extends IDataRow> extends ISheet<T> {

    Language getLanguage();

    ByteBuffer getBuffer();
}