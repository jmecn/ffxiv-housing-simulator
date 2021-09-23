package ffxiv.housim.app.state.indoor;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.simsilica.es.EntityData;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.PopupState;
import com.simsilica.lemur.style.ElementId;
import ffxiv.housim.app.core.enums.HouseType;
import ffxiv.housim.db.DBHelper;
import ffxiv.housim.ui.gui.DarkStyle;
import ffxiv.housim.ui.lemur.dropdown.Dropdown;
import ffxiv.housim.ui.lemur.window.*;
import lombok.NonNull;

import java.util.ResourceBundle;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/14
 */
public class HouseCreateState extends BaseAppState {

    // Resource bundle for i18n.
    ResourceBundle i18n = ResourceBundle.getBundle("ffxiv.housim.i18n/House");

    private DBHelper db;

    private EntityData ed;

    public HouseCreateState(EntityData ed) {
        this.ed = ed;
        this.db = DBHelper.INSTANCE;
    }

    private Dialog dialog;

    @Override
    protected void initialize(Application app) {
        dialog = getWindow();

        GuiGlobals.getInstance().setCursorEventsEnabled(false, false);
        // SimpleWindowManager popupState = app.getStateManager().getState(SimpleWindowManager.class);
        // popupState.showDialog(dialog);
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

    private Dialog getWindow() {

        Dropdown<String> houseType = new Dropdown<>();

        for (HouseType e : HouseType.values()) {
            houseType.getModel().add(i18n.getString(e.getDesc()));
        }

        Container main = new Container(new SpringGridLayout(), DarkStyle.STYLE);
        main.addChild(new Label(i18n.getString("house.create.type")));
        main.addChild(houseType);

        JmeDialog dialog = new JmeDialog(i18n.getString("house.create.title"), main);

        return dialog;
    }
}
