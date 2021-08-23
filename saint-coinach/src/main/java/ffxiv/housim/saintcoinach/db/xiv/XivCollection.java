package ffxiv.housim.saintcoinach.db.xiv;

import com.google.common.reflect.ClassPath;
import ffxiv.housim.saintcoinach.db.ex.Header;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.ISheet;
import ffxiv.housim.saintcoinach.db.ex.relational.*;
import ffxiv.housim.saintcoinach.db.xiv.sheets.InventoryItemSheet;
import ffxiv.housim.saintcoinach.db.xiv.sheets.ItemActionSheet;
import ffxiv.housim.saintcoinach.ex.*;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class XivCollection extends RelationalExCollection {
    private final static String PACKAGE_NAME = "ffxiv.housim.saintcoinach.xiv.entity";

    private Map<String, Class<IXivRow>> sheetNameToTypeMap;

    public XivCollection(PackCollection packCollection) {
        this(packCollection, null);
    }

    public XivCollection(PackCollection packCollection, File libraDatabase) {
        super(packCollection);

        if (libraDatabase != null && libraDatabase.exists()) {
            // TODO add support for libra
        }
    }

    public <T extends IXivRow> IXivSheet<T> getSheet(Class<T> t) {
        XivName attr = t.getAnnotation(XivName.class);
        String name = attr != null ? attr.value() : t.getSimpleName();
        return getSheet(name, t);
    }

    @SuppressWarnings("unchecked")
    public <T extends IXivRow> IXivSheet<T> getSheet(String name, Class<T> t) {
        return (IXivSheet<T>) getSheet(name);
    }

    @SuppressWarnings("unchecked")
    protected ISheet<?> createSheet(Header header) {
        IRelationalSheet baseSheet = (IRelationalSheet) super.createSheet(header);
        String name = baseSheet.getHeader().getName();
        if ("Item".equals(name)) {
            return new InventoryItemSheet(this, baseSheet);
        } else if ("ItemAction".equals(name)) {
            return new ItemActionSheet(this, baseSheet);
        }

        Class<IXivRow> match = getXivRowType(name);
        if (match != null) {
            Class genericType = baseSheet.getHeader().getVariant() == 2 ? XivSheet2.class : XivSheet.class;

            Constructor<ISheet<?>> constructor;
            try {
                constructor = genericType.getConstructor(XivCollection.class, IRelationalSheet.class, Class.class);
                return constructor.newInstance(this, baseSheet, match);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (header.getVariant() == 2) {
            return new XivSheet2<>(this, baseSheet, XivSubRow.class);
        } else {
            return new XivSheet<>(this, baseSheet, XivRow.class);
        }
    }

    private Class<IXivRow> getXivRowType(String name) {
        if (sheetNameToTypeMap == null) {
            buildSheetToTypeMap();
        }

        return sheetNameToTypeMap.get(name);
    }

    @SuppressWarnings("unchecked")
    private void buildSheetToTypeMap() {
        sheetNameToTypeMap = new ConcurrentHashMap<>();

        ClassPath classPath;
        try {
            classPath = ClassPath.from(getClass().getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to scan sheet type");
            return;
        }

        for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(PACKAGE_NAME)) {
            Class<?> clazz = classInfo.load();

            if (!IXivRow.class.isAssignableFrom(clazz)) {
                continue;
            }
            if (clazz.isInterface()) {
                continue;
            }
            if (Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }
            XivName attr = clazz.getAnnotation(XivName.class);
            String sheetName = attr != null ? attr.value() : classInfo.getSimpleName();

            log.debug("{} -> {}", sheetName, classInfo.getName());

            sheetNameToTypeMap.put(sheetName, (Class<IXivRow>) clazz);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ISheet<?> createSheet(Header header, Class<? extends IDataRow> clazz) {
        RelationalHeader relHeader = (RelationalHeader) header;
        Class<? extends IRelationalDataRow> klass = (Class<? extends IRelationalDataRow>) clazz;
        if (header.getAvailableLanguages().length > 1) {
            return new RelationalMultiSheet<>(this, relHeader, RelationalMultiRow.class, klass);
        } else {
            return new RelationalDataSheet<>(this, relHeader, header.getAvailableLanguages()[0], klass);
        }
    }
}
