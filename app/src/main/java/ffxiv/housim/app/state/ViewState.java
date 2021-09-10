package ffxiv.housim.app.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import ffxiv.housim.app.es.DyeColor;
import ffxiv.housim.app.es.Model;
import ffxiv.housim.app.es.Position;
import ffxiv.housim.app.es.Rotation;
import ffxiv.housim.app.factory.ModelFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ViewState extends BaseAppState {
    private EntityData ed;

    private EntitySet entities;

    private Node rootNode;
    private final Node viewRoot;

    private final Map<EntityId, Spatial> models;
    public ViewState(EntityData ed) {
        this.ed = ed;
        models = new HashMap<>();
        viewRoot = new Node("ViewRoot");
    }

    @Override
    protected void initialize(Application app) {
        if (app instanceof SimpleApplication simpleApp) {
            rootNode = simpleApp.getRootNode();
            rootNode.attachChild(viewRoot);
        }

        entities = ed.getEntities(Model.class, Position.class, Rotation.class, DyeColor.class);
    }

    @Override
    protected void cleanup(Application app) {
        entities.release();
        entities = null;
    }

    @Override
    protected void onEnable() {
        if (viewRoot.getParent() != rootNode) {
            rootNode.attachChild(viewRoot);
        }
    }

    @Override
    protected void onDisable() {
        if (viewRoot.getParent() == rootNode) {
            viewRoot.removeFromParent();
        }
    }

    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            removeEntities(entities.getRemovedEntities());
            updateEntities(entities.getChangedEntities());
            addEntities(entities.getAddedEntities());
        }
    }

    private void removeEntities(Set<Entity> entities) {
        for(Entity e : entities) {
            Spatial s = models.get(e.getId());
            s.removeFromParent();
        }
    }

    private void updateEntities(Set<Entity> entities) {
        for(Entity e : entities) {
            Spatial s = models.get(e.getId());
            updateModel(e, s);
        }
    }


    private void addEntities(Set<Entity> entities) {
        for(Entity e : entities) {
            Spatial s = createVisual(e);
            models.put(e.getId(), s);
            updateModel(e, s);
            viewRoot.attachChild(s);
        }
    }

    private Spatial createVisual(Entity e) {
        Model model = e.get(Model.class);

        Spatial s = ModelFactory.load(model.getPath());
        if (s != null) {
            s.setUserData("EntityId", e.getId());
            return s;
        }
        // TODO
        return new Node();
    }

    private void updateModel(Entity e, Spatial s) {
        Position p = e.get(Position.class);
        Rotation r = e.get(Rotation.class);
        DyeColor d = e.get(DyeColor.class);

        s.setLocalTranslation(p.getLocation());
        s.setLocalRotation(r.getRotation());
        s.depthFirstTraversal(it -> {
            if (it instanceof Geometry geom) {
                Material mat = geom.getMaterial();
                String matName = mat.getMaterialDef().getName();
                log.info("matName:{}", matName);
            }
        }, Spatial.DFSMode.PRE_ORDER );
    }
}
