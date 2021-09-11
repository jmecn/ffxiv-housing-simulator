package ffxiv.housim.app.state.indoor;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.*;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.InsetsComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedList;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.list.DefaultCellRenderer;
import ffxiv.housim.db.DBHelper;
import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.entity.Furniture;
import ffxiv.housim.db.entity.FurnitureCatalog;
import ffxiv.housim.db.mapper.FurnitureCatalogMapper;
import ffxiv.housim.db.mapper.FurnitureMapper;
import ffxiv.housim.saintcoinach.db.xiv.entity.housing.HousingItemCategory;
import ffxiv.housim.ui.lemur.icon.SqpackIcon;
import ffxiv.housim.ui.lemur.window.JmeWindow;
import ffxiv.housim.ui.lemur.window.SimpleWindowManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author yanmaoyuan
 * @date 2021/9/11
 */
@Slf4j
public class FurnitureState extends BaseAppState {
    private DBHelper db;

    Camera cam;
    List<FurnitureCatalog> catalogs;
    VersionedList<Furniture> furnitureList = new VersionedList<>();

    List<RollupPanel> rollupPanels = new ArrayList<>();
    List<VersionedReference<Boolean>> checkBoxes = new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        cam = app.getCamera();
        db = DBHelper.INSTANCE;

        try (SqlSession session = db.getSession(XivDatabase.FFXIV)) {
            FurnitureCatalogMapper furnitureCatalogMapper = session.getMapper(FurnitureCatalogMapper.class);
            catalogs = furnitureCatalogMapper.queryAll();
        }

        initGui();

        SimpleWindowManager windowManager = app.getStateManager().getState(SimpleWindowManager.class);
        windowManager.add(window);
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

    private JmeWindow window;

    private void initGui() {
        if (window != null) {
            return;
        }
        window = new JmeWindow("FurnitureCatalog");

        Container main = new Container();
        window.setContent(main);

        main.setLayout(new BorderLayout());

        // right
        Container right = new Container("Right");
        right.setBorder(new InsetsComponent(3, 3, 3, 1));
        right.setLayout(new SpringGridLayout());

        ListBox<Furniture> furnitureListBox = new ListBox<>(furnitureList);
        furnitureListBox.setCellRenderer(new FurnitureRenderer());
        furnitureListBox.addClickCommands(cmd -> {
            int selection = cmd.getSelectionModel().getSelection();
            Furniture f = (Furniture) cmd.getModel().get(selection);
            log.info("id:{}, name:{}", f.getId(), f.getName());
            // TODO set current furniture
        });
        right.addChild(furnitureListBox);

        // left
        Container left = new Container("Left");
        left.setBorder(new InsetsComponent(3, 3, 3, 1));
        left.setLayout(new SpringGridLayout());

        main.addChild(right, BorderLayout.Position.Center);
        main.addChild(left, BorderLayout.Position.West);

        // init left
        initLeft(left);
    }

    class FurnitureRenderer extends DefaultCellRenderer<Furniture> {
        @Override
        public Panel getView(Furniture value, boolean selected, Panel existing) {
            if( existing == null ) {
                Button button = new Button(value.getName(), getElement(), getStyle());
                button.setIcon(getIcon(value.getIcon()));
                button.setPreferredSize(new Vector3f(200, 32, 0));
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
            return component;
        }
    }

    private void initLeft(Container left) {

        // init left
        TreeMap<Integer, List<FurnitureCatalog>> map = catalogs.stream().collect(Collectors.groupingBy(FurnitureCatalog::getCategory, TreeMap::new, Collectors.toList()));

        int i = 0;
        for (Map.Entry<Integer, List<FurnitureCatalog>> e : map.entrySet()) {
            HousingItemCategory cat = HousingItemCategory.of(e.getKey());
            List<FurnitureCatalog> sub = e.getValue();

            FurnitureCatalog all = new FurnitureCatalog();
            all.setCategory(e.getKey());
            all.setId(null);
            all.setName("全部");

            FurnitureCatalog spec = new FurnitureCatalog();
            spec.setCategory(e.getKey());
            spec.setId(999);
            spec.setName("???");

            ListBox<FurnitureCatalog> listBox = new ListBox<>();

            listBox.getModel().add(all);
            listBox.getModel().addAll(sub);
            listBox.getModel().add(spec);

            listBox.getGridPanel().setVisibleRows(listBox.getModel().size());
            listBox.setScrollOnHover(false);// disable scroll
            listBox.getSlider().removeFromParent();// disable slider

            RollupPanel rollupPanel = new RollupPanel(cat.getName(), "glass");
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

            log.info("list:{}", listBox.getPreferredSize());
            listBox.setSize(new Vector3f(200, listBox.getPreferredSize().y, 0f));
            left.addChild(rollupPanel);

            listBox.addClickCommands(cmd -> {
                int selection = cmd.getSelectionModel().getSelection();
                FurnitureCatalog fc = (FurnitureCatalog) cmd.getModel().get(selection);
                log.info("cat:{}, id:{}, name:{}", fc.getCategory(), fc.getId(), fc.getName());
                queryFurniture(fc.getCategory(), fc.getId());
            });
        }
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
}
