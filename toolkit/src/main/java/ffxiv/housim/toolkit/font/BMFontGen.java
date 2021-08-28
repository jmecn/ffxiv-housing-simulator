package ffxiv.housim.toolkit.font;

import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingFurniture;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class BMFontGen {

    Set<Character> texts;
    ARealmReversed db;

    public BMFontGen() throws IOException {
        String gameDir = System.getenv("FFXIV_HOME");
        db = new ARealmReversed(gameDir, Language.ChineseSimplified);

        texts = new TreeSet<>();
    }

    public void start() throws IOException {
        collectChars();
        save(new File("fur.txt"));
    }

    private void save(File out) throws IOException {
        StringBuilder buffer = new StringBuilder(texts.size());
        for (char c : texts) {
            buffer.append(c);
        }
        Files.writeString(out.toPath(), buffer.toString(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public void collectChars() {
        addAscii();
        addItemNames();
        addFurnitureNames();
    }

    private void addAscii() {
        // ascii
        for (char c = 0x20; c < 0x79; c++) {
            texts.add(c);
        }
    }

    private void addFurnitureNames() {
        foreach(HousingFurniture.class, e-> {
            Item item = e.getItem();
            add(item.getName());
        });
    }

    private void addItemNames() {
        IXivSheet<Item> items = db.getGameData().getSheet(Item.class);
        for (Item i : items) {
            add(i.getName());
        }

    }

    public interface Visitor<T> {
        void visit(T e);
    }

    public <T extends IXivRow> void foreach(Class<T> clazz, Visitor<T> visitor) {
        IXivSheet<T> sheet = db.getGameData().getSheet(clazz);
        log.info("Visit sheet: {}, row count: {}", sheet.getName(), sheet.getCount());
        for (T xivRow : sheet) {
            visitor.visit(xivRow);
        }
    }

    private void add(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        for (int i = 0; i < value.length(); i++) {
            Character s = value.charAt(i);
            if (texts.contains(s)) {
                continue;
            }
            texts.add(s);
        }
    }

    public static void main(String[] args) throws IOException {
        BMFontGen app = new BMFontGen();
        app.start();
    }
}
