package ffxiv.housim.saintcoinach.db.xiv.item;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.param.Parameter;
import ffxiv.housim.saintcoinach.db.xiv.param.ParameterCollection;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.param.IParameterObject;

/**
 * Base class for equipment items.
 */
// TODO need implements
public abstract class Equipment extends Item implements IParameterObject {

    private ParameterCollection allParameters;

    private ParameterCollection secondaryParameters;

    protected Equipment(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public Iterable<Parameter> getAllParameters() {
        if (allParameters == null) {
            // TODO build AllParameters
        }
        return allParameters;
    }

    public Iterable<Parameter> getParameters() {
        return getAllParameters();
    }
}
