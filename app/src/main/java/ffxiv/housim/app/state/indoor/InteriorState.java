package ffxiv.housim.app.state.indoor;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.props.PropertyPanel;
import com.simsilica.lemur.text.DefaultDocumentModel;
import com.simsilica.lemur.text.DocumentModel;
import ffxiv.housim.app.core.indoor.Blueprint;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/3
 */
public class InteriorState extends BaseAppState {
    DocumentModel model;

    Blueprint blueprint;
    @Override
    protected void initialize(Application app) {
        model = new DefaultDocumentModel();

        blueprint = new Blueprint();
        PropertyPanel panel = new PropertyPanel("glass");
        panel.addEnumField("houseType", blueprint, "houseType");
        panel.addEnumField("area", blueprint, "area");
        panel.addIntField("limit", blueprint, "limit", 200, 400, 100);

        if (app instanceof SimpleApplication sa) {
            sa.getGuiNode().attachChild(panel);
            panel.setLocalTranslation(100, 300, 0);
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
}
