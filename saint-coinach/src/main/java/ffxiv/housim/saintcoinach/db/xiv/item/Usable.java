package ffxiv.housim.saintcoinach.db.xiv.item;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.entity.Item;
import ffxiv.housim.saintcoinach.db.xiv.param.IParameterObject;
import ffxiv.housim.saintcoinach.db.xiv.param.Parameter;

// TODO need implements
public class Usable extends Item implements IParameterObject {
    public Usable(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    @Override
    public Iterable<Parameter> getParameters() {
        // TODO implements ItemAction first
        return null;
    }
}
