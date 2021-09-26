package ffxiv.housim.app.state.indoor;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.*;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedList;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.list.DefaultCellRenderer;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.text.DefaultDocumentModel;
import ffxiv.housim.app.es.DyeColor;
import ffxiv.housim.app.es.Model;
import ffxiv.housim.app.es.Position;
import ffxiv.housim.app.es.Rotation;
import ffxiv.housim.db.DBHelper;
import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.entity.Furniture;
import ffxiv.housim.db.entity.FurnitureCatalog;
import ffxiv.housim.db.mapper.FurnitureCatalogMapper;
import ffxiv.housim.db.mapper.FurnitureMapper;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingItemCategory;
import ffxiv.housim.ui.gui.DarkStyle;
import ffxiv.housim.ui.lemur.icon.SqpackIcon;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author yanmaoyuan
 * @date 2021/9/11
 */
@Slf4j
public class FurnitureCatalogState extends BaseAppState {

    // Resource bundle for i18n.
    ResourceBundle i18n = ResourceBundle.getBundle("ffxiv.housim.i18n/House");

    private DBHelper db;

    Camera cam;

    DefaultDocumentModel searchDoc = new DefaultDocumentModel();

    VersionedList<Furniture> furnitureList = new VersionedList<>();

    List<RollupPanel> rollupPanels = new ArrayList<>();
    List<VersionedReference<Boolean>> checkBoxes = new ArrayList<>();

    private EntityId entityId;

    private EntityData ed;

    public FurnitureCatalogState(EntityData ed) {
        this.ed = ed;
        this.db = DBHelper.INSTANCE;
    }

    private Node guiNode;

    private Container window;

    @Override
    protected void initialize(Application app) {

        cam = app.getCamera();

        if (app instanceof SimpleApplication simpleApp) {
            guiNode = simpleApp.getGuiNode();
        }

        window = getWindow();
        guiNode.attachChild(window);
        window.setLocalTranslation(20, 500, 10);
        CursorEventControl.addListenersToSpatial(window, new DragHandler());
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    class FurnitureRenderer extends DefaultCellRenderer<Furniture> {
        @Override
        public Panel getView(Furniture value, boolean selected, Panel existing) {
            if( existing == null ) {
                Button button = new Button(value.getName(), new ElementId("list.item"), "DarkStyle.STYLE");
                button.setIcon(getIcon(value.getIcon()));
                button.setPreferredSize(new Vector3f(200, 20, 0));
                existing = button;
            } else {
                Button button = (Button) existing;
                button.setText(value.getName());
                button.setIcon(getIcon(value.getIcon()));
            }
            return existing;
        }

        private SqpackIcon getIcon(String path) {
            SqpackIcon component = new SqpackIcon(path, 1f, 0, 0, 0.01f, false);
            component.setIconSize(new Vector2f(16, 16));
            return component;
        }
    }

    private Container getWindow() {
        Container main = new Container(new BorderLayout());

        // north
        Label title = new Label(i18n.getString("furniture.catalog.title"));
        Container catalog = getCatalogPanel();
        Container search = getSearchPanel();

        Container west = new Container(new BorderLayout());
        west.addChild(search, BorderLayout.Position.North);
        west.addChild(catalog, BorderLayout.Position.Center);

        Container center = getCenterPanel();

        Container south = getButtonPanel();

        main.addChild(title, BorderLayout.Position.North);
        main.addChild(center, BorderLayout.Position.Center);
        main.addChild(west, BorderLayout.Position.West);
        main.addChild(south, BorderLayout.Position.South);

        return main;
    }

    private Container getCenterPanel() {
        Container right = new Container(new SpringGridLayout());

        ListBox<Furniture> furnitureListBox = new ListBox<>(furnitureList);
        furnitureListBox.setCellRenderer(new FurnitureRenderer());
        furnitureListBox.getGridPanel().setVisibleRows(20);
        furnitureListBox.addClickCommands(cmd -> {
            int selection = cmd.getSelectionModel().getSelection();
            Furniture f = (Furniture) cmd.getModel().get(selection);
            setCurrentFurniture(f);
        });
        right.addChild(furnitureListBox);

        return right;
    }

    private Container getSearchPanel() {
        Container panel = new Container();
        panel.setInsets(new Insets3f(0, 0, 2, 0));

        panel.addChild(new Label(i18n.getString("furniture.catalog.search")));
        TextField textField = panel.addChild(new TextField(searchDoc));
        textField.setSingleLine(true);

        return panel;
    }

    private Container getCatalogPanel() {
        Container catalogPanel = new Container();
        catalogPanel.setLayout(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Last, FillMode.Even));

        catalogPanel.addChild(new Label(i18n.getString("furniture.catalog.catalog")));

        List<FurnitureCatalog> catalogs;
        try (SqlSession session = db.getSession(XivDatabase.FFXIV)) {
            FurnitureCatalogMapper furnitureCatalogMapper = session.getMapper(FurnitureCatalogMapper.class);
            catalogs = furnitureCatalogMapper.queryAll();
        }

        // init left
        TreeMap<Integer, List<FurnitureCatalog>> map = catalogs.stream().collect(Collectors.groupingBy(FurnitureCatalog::getCategory, TreeMap::new, Collectors.toList()));

        int i = 0;
        for (Map.Entry<Integer, List<FurnitureCatalog>> e : map.entrySet()) {
            HousingItemCategory cat = HousingItemCategory.of(e.getKey());
            List<FurnitureCatalog> sub = e.getValue();

            ListBox<FurnitureCatalog> listBox = new ListBox<>();

            int specCount = queryCount(e.getKey(), 999);
            int catCount = e.getValue().size();

            // 有多个分类，增加一个"全部"选项
            if (catCount > 1 || specCount > 0) {
                FurnitureCatalog all = new FurnitureCatalog();
                all.setCategory(e.getKey());
                all.setId(null);
                all.setName("全部");
                listBox.getModel().add(all);
            }

            listBox.getModel().addAll(sub);

            // 增加一个代表未知家具的选项
            if (specCount > 0) {
                FurnitureCatalog spec = new FurnitureCatalog();
                spec.setCategory(e.getKey());
                spec.setId(XivDatabase.UNKNOWN_CATALOG);
                spec.setName("???");

                listBox.getModel().add(spec);
            }

            int rows = listBox.getModel().size();
            if (rows < 5) {
                rows = 5;
            }
            listBox.getGridPanel().setVisibleRows(rows);
            listBox.getGridPanel().setRow(rows);

            listBox.setScrollOnHover(false);// disable scroll
            listBox.getSlider().removeFromParent();// disable slider

            RollupPanel rollupPanel = new RollupPanel(cat.getName(), DarkStyle.STYLE);
            rollupPanel.setContents(listBox);
            if (i++ == 0) {
                rollupPanel.setOpen(true);
                listBox.getSelectionModel().setSelection(0);
                queryFurniture(e.getKey(), null);
            } else {
                rollupPanel.setOpen(false);
            }

            rollupPanels.add(rollupPanel);
            checkBoxes.add(rollupPanel.getOpenModel().createReference());

            catalogPanel.addChild(rollupPanel);

            listBox.addClickCommands(cmd -> {
                int selection = cmd.getSelectionModel().getSelection();
                FurnitureCatalog fc = (FurnitureCatalog) cmd.getModel().get(selection);
                queryFurniture(fc.getCategory(), fc.getId());
            });
        }

        // Just a placeholder, make sure that left container is wide enough.
        Panel panel = catalogPanel.addChild(new Panel(DarkStyle.STYLE));
        panel.setPreferredSize(new Vector3f(140, 1, 0));
        return catalogPanel;
    }

    private int queryCount(Integer category, Integer catalog) {
        int count = 0;
        try (SqlSession session = db.getSession(XivDatabase.FFXIV)) {
            FurnitureMapper mapper = session.getMapper(FurnitureMapper.class);
            count = mapper.queryCount(category, catalog);
        }
        return count;
    }

    private void queryFurniture(Integer category, Integer catalog) {
        try (SqlSession session = db.getSession(XivDatabase.FFXIV)) {
            FurnitureMapper furnitureMapper = session.getMapper(FurnitureMapper.class);
            List<Furniture> list = furnitureMapper.query(category, catalog);
            furnitureList.clear();
            for (Furniture it : list) {
                furnitureList.add(it);
            }
        }
    }

    private Container getButtonPanel() {
        Container panel = new Container(DarkStyle.STYLE);

        Button add = panel.addChild(new Button(i18n.getString("furniture.catalog.add")));
        add.setInsetsComponent(new DynamicInsetsComponent(0.5f, 1.0f, 0.5f,0.0f));
        add.addClickCommands(cmd -> {
            // TODO
        });
        return panel;
    }

    @Override
    public void update(float tpf) {
        for (int i = 0; i < checkBoxes.size(); i++) {
            VersionedReference<Boolean> check = checkBoxes.get(i);
            if (check.update()) {
                if (check.get()) {
                    for (int j = 0; j < rollupPanels.size(); j++) {
                        if (j == i) continue;
                        rollupPanels.get(j).setOpen(false);
                    }
                }
            }
        }
    }

    public void setCurrentFurniture(Furniture furniture) {
        log.info("id:{}, name:{}", furniture.getId(), furniture.getName());

        if (entityId == null) {
            entityId = ed.createEntity();
        }

        ed.setComponent(entityId, new Model(furniture.getModel()));
        ed.setComponent(entityId, new Position(new Vector3f()));
        ed.setComponent(entityId, new Rotation(0f));
        ed.setComponent(entityId, new DyeColor(null));
    }
}
