package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.Range;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

@Slf4j
public class Header {

    private int columnCount;
    private int rangeCount;
    private int languageCount;

    private Column[] mColumns;
    private Range[] dataFileRanges;
    private Language[] availableLanguages;

    @Getter
    private ExCollection collection;
    @Getter
    private PackFile file;
    @Getter
    private String name;
    @Getter
    private int variant;
    @Getter
    private List<Column> columns;
    public int getColumnCount() {
        return columns.size();
    }
    @Getter
    private int fixedSizeDataLength;

    public Header(ExCollection collection, String name, PackFile file) {
        this.collection = collection;
        this.name = name;
        this.file = file;

        build();
    }

    private void build() {
        byte[] data = file.getData();

        if (data.length < 0x2E) {
            throw new IllegalStateException("EXH file is too short");
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int magic = buffer.getInt();
        if (magic != 0x46485845) {// EXHF
            throw new IllegalArgumentException("File not a EX header");
        }

        buffer.order(ByteOrder.BIG_ENDIAN);

        int value1 = buffer.getShort();// skip 2 bytes;
        fixedSizeDataLength = buffer.getShort();
        columnCount = buffer.getShort();
        rangeCount = buffer.getShort();
        languageCount = buffer.getShort();

        buffer.position(0x10);
        variant = buffer.getShort();
        if (variant != 1 && variant != 2) {
            throw new UnsupportedOperationException("Unknown variant " + variant);
        }

        buffer.position(0x20);
        readColumns(buffer);
        readPartialFiles(buffer);
        readSuffixes(buffer);
    }

    private void readColumns(ByteBuffer buffer) {
        mColumns = new Column[columnCount];
        for (int i = 0; i < columnCount; i++) {
            // 4 bytes
            int type = buffer.getShort();
            int offset = buffer.getShort();
            mColumns[i] = new Column(this, i, type, offset);

            ColumnType columnType = ColumnType.of(type);
            log.info("index:{}, type:{}, offset:{}", i, columnType, offset);
        }
    }

    private void readPartialFiles(ByteBuffer buffer) {
        dataFileRanges = new Range[rangeCount];
        for (int i = 0; i < rangeCount; i++) {
            int min = buffer.getInt();
            int len = buffer.getInt();
            dataFileRanges[i] = new Range(min, len);
            log.info("page:{}, start:{}, length:{}", i, min, len);
        }
    }

    private void readSuffixes(ByteBuffer buffer) {

        // ScreenImage and CutScreenImage reference localized image files,
        // however their available languages are only None.  Perhaps there
        // is a flag to use a global list of available languages in this
        // buffer?
        availableLanguages = new Language[languageCount];
        for (int i = 0; i < languageCount; i++) {
            int lang = buffer.get();
            availableLanguages[i] = Language.of(lang);
            log.info("lang:{}", availableLanguages[i]);
        }
    }
}
