package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.EntityData;
import lombok.Getter;

public class EntityDataState extends BaseAppState {
    @Getter
    EntityData ed;

    public EntityDataState(EntityData ed) {
        this.ed = ed;
    }

    @Override
    protected void initialize(Application app) {

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
