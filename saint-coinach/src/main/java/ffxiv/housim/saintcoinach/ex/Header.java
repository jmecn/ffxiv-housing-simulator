package ffxiv.housim.saintcoinach.ex;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class Header {

    @Getter
    private int columnCount;
    private int pageCount;
    private int languageCount;
    @Getter
    private Column[] columns;
    @Getter
    private Page[] pages;
    @Getter
    private Language[] availableLanguages;

    @Getter
    private final ExCollection collection;
    @Getter
    private final PackFile file;
    @Getter
    private final String name;
    @Getter
    private int variant;
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

        buffer.getShort();// skip 2 bytes;
        fixedSizeDataLength = buffer.getShort();
        columnCount = buffer.getShort();
        pageCount = buffer.getShort();
        languageCount = buffer.getShort();

        buffer.position(0x10);
        variant = buffer.getShort();
        if (variant != 1 && variant != 2) {
            throw new UnsupportedOperationException("Unknown variant " + variant);
        }

        buffer.position(0x20);
        readColumns(buffer);
        readPages(buffer);
        readLocales(buffer);
    }

    private void readColumns(ByteBuffer buffer) {
        columns = new Column[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columns[i] = createColumn(i, buffer);
        }
    }

    protected Column createColumn(int index, ByteBuffer buffer) {
        // 4 bytes
        int type = buffer.getShort();
        int offset = buffer.getShort();
        return new Column(this, index, type, offset);
    }

    private void readPages(ByteBuffer buffer) {
        pages = new Page[pageCount];
        for (int i = 0; i < pageCount; i++) {
            int min = buffer.getInt();
            int len = buffer.getInt();
            pages[i] = new Page(min, len);
        }
    }

    private void readLocales(ByteBuffer buffer) {

        // ScreenImage and CutScreenImage reference localized image files,
        // however their available languages are only None.  Perhaps there
        // is a flag to use a global list of available languages in this
        // buffer?
        availableLanguages = new Language[languageCount];
        for (int i = 0; i < languageCount; i++) {
            int lang = buffer.get();
            availableLanguages[i] = Language.of(lang);
        }
        log.debug("availableLanguages: {}", availableLanguages);
    }

    public Column getColumn(int columnIndex) {
        return columns[columnIndex];
    }
}
