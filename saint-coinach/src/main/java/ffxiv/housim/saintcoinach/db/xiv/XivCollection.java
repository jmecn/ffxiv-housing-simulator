package ffxiv.housim.saintcoinach.db.xiv;

import com.google.common.reflect.ClassPath;
import ffxiv.housim.saintcoinach.db.ex.Header;
import ffxiv.housim.saintcoinach.db.ex.IDataRow;
import ffxiv.housim.saintcoinach.db.ex.ISheet;
import ffxiv.housim.saintcoinach.db.ex.relational.*;
import ffxiv.housim.saintcoinach.db.libra.Entities;
import ffxiv.housim.saintcoinach.db.xiv.collections.*;
import ffxiv.housim.saintcoinach.db.xiv.sheets.InventoryItemSheet;
import ffxiv.housim.saintcoinach.db.xiv.sheets.ItemActionSheet;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link RelationalExCollection} for OO representations of FFXIV game data.
 */
@Slf4j
public class XivCollection extends RelationalExCollection {

    /**
     * Mapping of sheet names to the object types to use for them.
     */
    private Map<String, Class<IXivRow>> sheetNameToTypeMap;

    /**
     * Collection of {@link ffxiv.housim.saintcoinach.db.xiv.entity.BNpc} objects.
     */
    private BNpcCollection bNpcs;

    /**
     * Collection of <see cref="ClassJobActionBase"/> (containing both <see cref="Action" /> and <see cref="CraftAction" />).
     */
    private ClassJobActionCollection classJobActions;

    /**
     * Collection of ENpc objects (containg data of both <see cref="ENpcBase" /> and <see cref="ENpcResident" />).
     */
    private ENpcCollection eNpcs;

    /**
     * Collection of equipment slots.
     */
    private EquipSlotCollection equipSlots;

    /**
     * Collection of items (containing both <see cref="Item" /> and <see cref="EventItem" />).
     */
    private ItemCollection items;

    /**
     * Collection of all shops.
     */
    private ShopCollection shops;

    /**
     * Database connection to Libra Eorzea data.
     */
    @Getter
    private Entities libra;

    public XivCollection(PackCollection packCollection) {
        this(packCollection, null);
    }

    public XivCollection(PackCollection packCollection, File libraDatabase) {
        super(packCollection);

        if (libraDatabase != null && libraDatabase.exists()) {
            // TODO add support for libra
        }
    }

    /**
     * Gets a value indicating whether the Libra Eorzea database is available.
     * @return A value indicating whether the Libra Eorzea database is available.
     */
    public boolean isLibraAvailable() {
        return libra != null;
    }

    /**
     * Gets the collection of {@link ffxiv.housim.saintcoinach.db.xiv.entity.BNpc} objects.
     *
     * This property is only supported when the Libra Eorzea database is present.
     *
     * @return The collection of {@link ffxiv.housim.saintcoinach.db.xiv.entity.BNpc} objects.
     */
    public BNpcCollection getBNpcs() {
        if (!isLibraAvailable()) {
            throw new UnsupportedOperationException("BNpcs are only available when Libra Eorzea database is present.");
        }
        if (bNpcs == null) {
            bNpcs = new BNpcCollection(this);
        }
        return bNpcs;
    }

    /**
     * Gets the collection of {@link ffxiv.housim.saintcoinach.db.xiv.entity.ClassJobActionBase} (containing both {@link ffxiv.housim.saintcoinach.db.xiv.entity.Action} and {@link ffxiv.housim.saintcoinach.db.xiv.entity.CraftAction}).
     *
     * @return The collection of {@link ffxiv.housim.saintcoinach.db.xiv.entity.ClassJobActionBase} (containing both {@link ffxiv.housim.saintcoinach.db.xiv.entity.Action} and {@link ffxiv.housim.saintcoinach.db.xiv.entity.CraftAction}).
     */
    public ClassJobActionCollection getClassJobActions() {
        if (classJobActions == null) {
            classJobActions = new ClassJobActionCollection(this);
        }
        return classJobActions;
    }

    /// <summary>
    ///     Gets the collection of ENpc objects (containg data of both <see cref="ENpcBase" /> and <see cref="ENpcResident" />
    ///     ).
    /// </summary>
    /// <value>The collection of ENpc objects (containg data of both <see cref="ENpcBase" /> and <see cref="ENpcResident" />).</value>
    public ENpcCollection getENpcs() {
        if (eNpcs == null) {
            eNpcs = new ENpcCollection(this);
        }
        return eNpcs;
    }

    /// <summary>
    ///     Gets the collection of equipment slots.
    /// </summary>
    /// <value>The collection of equipment slots.</value>
    public EquipSlotCollection getEquipSlots() {
        if (equipSlots == null) {
            equipSlots = new EquipSlotCollection(this);
        }
        return equipSlots;
    }

    /// <summary>
    ///     Gets the collection of items (containing both <see cref="Item" /> and <see cref="EventItem" />).
    /// </summary>
    /// <value>The collection of items (containing both <see cref="Item" /> and <see cref="EventItem" />).</value>
    public ItemCollection getItems() {
        if (items == null) {
            items = new ItemCollection(this);
        }
        return items;
    }

    /// <summary>
    ///     Gets the collection of all shops.
    /// </summary>
    /// <value>The collection of all shops.</value>
    public ShopCollection getShops() {
        if (shops == null) {
            shops = new ShopCollection(this);
        }
        return shops;
    }

    public <T extends IXivRow> IXivSheet<T> getSheet(Class<T> t) {
        XivName attr = t.getAnnotation(XivName.class);
        String name = attr != null ? attr.value() : t.getSimpleName();
        return getSheet(name, t);
    }

    public <T extends IXivSubRow> XivSheet2<T> getSheet2(Class<T> t) {
        XivName attr = t.getAnnotation(XivName.class);
        String name = attr != null ? attr.value() : t.getSimpleName();
        return getSheet2(name, t);
    }

    @SuppressWarnings("unchecked")
    public <T extends IXivRow> IXivSheet<T> getSheet(String name, Class<T> t) {
        return (IXivSheet<T>) getSheet(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends IXivSubRow> XivSheet2<T> getSheet2(String name, Class<T> t) {
        return (XivSheet2<T>) getSheet(name);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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

    @SuppressWarnings({"unchecked"})
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

        Class<?> c = getClass();

        // getPackageName
        String pn;
        if (c.isPrimitive()) {
            pn = "java.lang";
        } else {
            String cn = c.getName();
            int dot = cn.lastIndexOf('.');
            pn = (dot != -1) ? cn.substring(0, dot).intern() : "";
        }
        String packageName = pn + ".entity";
        for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(packageName)) {
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
