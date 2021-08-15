package ffxiv.housim.saintcoinach.xiv.item;

import ffxiv.housim.saintcoinach.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.xiv.entity.Item;
import ffxiv.housim.saintcoinach.xiv.param.IParameterObject;
import ffxiv.housim.saintcoinach.xiv.param.Parameter;
import ffxiv.housim.saintcoinach.xiv.param.ParameterCollection;

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
