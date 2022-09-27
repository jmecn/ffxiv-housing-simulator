package ffxiv.housim.app.state.indoor;

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
import ffxiv.housim.app.es.interior.HModel;
import ffxiv.housim.app.es.interior.HType;
import ffxiv.housim.app.factory.ModelFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yanmaoyuan
 * @date 2021/9/13
 */
@Slf4j
public class HouseViewState extends BaseAppState {
    private EntityData ed;

    private EntitySet entities;

    private Node rootNode;
    private final Node viewRoot;

    private final Map<EntityId, Spatial> models;
    private final Map<EntityId, String> modelNames;

    public HouseViewState(EntityData ed) {
        this.ed = ed;
        models = new HashMap<>();
        modelNames = new HashMap<>();
        viewRoot = new Node("ViewRoot");
    }

    @Override
    protected void initialize(Application app) {
        if (app instanceof SimpleApplication) {
            SimpleApplication simpleApp = (SimpleApplication) app;
            rootNode = simpleApp.getRootNode();
            rootNode.attachChild(viewRoot);
        }

        entities = ed.getEntities(HModel.class, HType.class);
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
            EntityId id = e.getId();

            Spatial s = models.get(id);
            s.removeFromParent();
            modelNames.remove(id);
            models.remove(id);
        }
    }

    private void updateEntities(Set<Entity> entities) {
        for(Entity e : entities) {
            updateVisual(e);
            Spatial s = models.get(e.getId());
            updateTranslation(e, s);
        }
    }


    private void addEntities(Set<Entity> entities) {
        for(Entity e : entities) {
            Spatial s = createVisual(e);
            models.put(e.getId(), s);
            modelNames.put(e.getId(), s.getName());
            updateTranslation(e, s);
            viewRoot.attachChild(s);
            log.info("add spatial:{}", s.getName());
        }
    }

    private Spatial createVisual(Entity e) {
        Model model = e.get(Model.class);

        Spatial s = ModelFactory.load(model.getPath());
        if (s != null) {
            s.setUserData("EntityId", e.getId().getId());
            return s;
        }
        // TODO
        return new Node();
    }

    private void updateVisual(Entity e) {
        Model m = e.get(Model.class);
        EntityId id = e.getId();

        if (!models.containsKey(id)) {
            Spatial s = createVisual(e);
            models.put(id, s);
            modelNames.put(id, m.getPath());
            viewRoot.attachChild(s);
        } else {
            String exist = modelNames.get(id);
            // modified?
            if (!exist.equals(m.getPath())) {
                modelNames.remove(id);
                Spatial s = models.remove(id);
                s.removeFromParent();

                // new model
                s = createVisual(e);
                models.put(id, s);
                modelNames.put(id, s.getName());
                viewRoot.attachChild(s);
            }
        }
    }

    private void updateTranslation(Entity e, Spatial s) {
        Model m = e.get(Model.class);
        Position p = e.get(Position.class);
        Rotation r = e.get(Rotation.class);
        DyeColor d = e.get(DyeColor.class);

        s.setLocalTranslation(p.getLocation());
        s.setLocalRotation(r.getRotation());
        s.depthFirstTraversal(it -> {
            if (it instanceof Geometry) {
                Geometry geom = (Geometry) it;
                Material mat = geom.getMaterial();
                String matName = mat.getMaterialDef().getName();
                log.info("matName:{}", matName);
            }
        }, Spatial.DFSMode.PRE_ORDER );
    }
}
