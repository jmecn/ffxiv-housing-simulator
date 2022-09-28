package ffxiv.housim.saintcoinach.db.xiv.music.orch;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class Orchestrion extends XivRow {

    private OrchestrionPath orchestrionPath;
    private OrchestrionUiparam orchestrionUiparam;

    public Orchestrion(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public String getDescription() {
        return asString("Description");
    }

    public OrchestrionPath getOrchestrionPath() {
        if (orchestrionPath == null) {
            orchestrionPath = getSheet().getCollection().getSheet(OrchestrionPath.class).get(getKey());
        }
        return orchestrionPath;
    }

    public OrchestrionUiparam getOrchestrionUiparam() {
        if (orchestrionUiparam == null) {
            orchestrionUiparam = getSheet().getCollection().getSheet(OrchestrionUiparam.class).get(getKey());
        }
        return orchestrionUiparam;
    }

    @Override
    public String toString() {
        return getName();
    }
}
