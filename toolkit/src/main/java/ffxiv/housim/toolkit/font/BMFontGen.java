package ffxiv.housim.toolkit.font;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import ffxiv.housim.saintcoinach.ARealmReversed;
import ffxiv.housim.saintcoinach.db.ex.Language;
import ffxiv.housim.saintcoinach.db.xiv.IXivRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.entity.Stain;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.FurnitureCatalogCategory;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingFurniture;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingItemCategory;
import ffxiv.housim.saintcoinach.db.xiv.entity.map.TerritoryType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class BMFontGen {

    Set<Character> texts;
    ARealmReversed db;

    public BMFontGen() throws IOException {
        String gameDir = System.getenv("FFXIV_HOME");
        log.info("gameDir:{}", gameDir);
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
        Files.write(buffer.toString().getBytes(StandardCharsets.UTF_8), out);
    }

    public void collectChars() {
        addAscii();
        addItemNames();
        addPlaceName();
        addFurnitureNames();
        addGui();
    }

    private void addAscii() {
        // ascii
        for (char c = 0x20; c < 0x79; c++) {
            texts.add(c);
        }
    }

    private void addPlaceName() {
        Set<String> map = Sets.newHashSet("s1i1", "s1i2", "s1i3", "s1i4",
                "f1i1", "f1i2", "f1i3", "f1i4",
                "w1i1", "w1i2", "w1i3", "w1i4",
                "e1i1", "e1i2", "e1i3", "e1i4");

        foreach(TerritoryType.class, e-> {
            String name = e.getName();

            if (!map.contains(name)) {
                return;
            }
            add(e.getPlaceName().getName());
            add(e.getRegionPlaceName().getName());
            add(e.getZonePlaceName().getName());
        });
    }

    private void addFurnitureNames() {
        foreach(HousingFurniture.class, e-> {
            Item item = e.getItem();
            add(item.getName());
        });

        for (HousingItemCategory e : HousingItemCategory.values()) {
            add(e.getName());
        }

        foreach(FurnitureCatalogCategory.class, e-> {
            add(e.getCategory());
        });
    }

    private void addItemNames() {
        IXivSheet<Item> items = db.getGameData().getSheet(Item.class);
        for (Item i : items) {
            if (i.getFilterGroup() == 14 || i.getFilterGroup() == 15) {
                add(i.getName());
                IXivRow row = i.getAdditionalData();
                if (row instanceof Stain) {
                    Stain stain = (Stain) row;
                    add(stain.getName());
                }
            }
        }

    }

    private void addGui() {
        add("文件");
        add("新建");
        add("家具装修蓝图");
        add("庭院装修蓝图");
        add("打开");
        add("打开最近");
        add("保存");
        add("另存为");
        add("退出");
        add("关闭");
        add("设置");
        add("帮助");
        add("关于");
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
