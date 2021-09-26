package ffxiv.housim.app.state.indoor;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.EntityData;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.props.PropertyPanel;
import com.simsilica.lemur.text.DefaultDocumentModel;
import com.simsilica.lemur.text.DocumentModel;
import ffxiv.housim.app.core.indoor.Blueprint;

import java.util.ResourceBundle;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/3
 */
public class InteriorState extends BaseAppState {
    private EntityData ed;

    DocumentModel model;

    Blueprint blueprint;

    // Resource bundle for i18n.
    ResourceBundle i18n = ResourceBundle.getBundle("ffxiv.housim.i18n/House");

    public InteriorState(EntityData ed) {
        this.ed = ed;
    }

    @Override
    protected void initialize(Application app) {
        model = new DefaultDocumentModel();

        blueprint = new Blueprint();
        PropertyPanel panel = new PropertyPanel("dark");
        panel.addEnumField("房屋类型", blueprint, "type");
        panel.addEnumField("住宅区", blueprint, "area");

        panel.setLocalTranslation(100, 300, 0);
        CursorEventControl.addListenersToSpatial(panel, new DragHandler());
        if (app instanceof SimpleApplication sa) {
            sa.getGuiNode().attachChild(panel);

        }
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

    private Container getWindow() {
        Container main = new Container(new BorderLayout());

        Label title = new Label(i18n.getString("house.create.title"));
        main.addChild(title, BorderLayout.Position.North);

        return main;
    }

    private Container getHouseType() {
        Container container = new Container();
        return container;
    }
}
