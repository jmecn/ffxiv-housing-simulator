package ffxiv.housim.saintcoinach.xiv.param;

import java.util.Collection;

/**
 * Interface for objects that offer parameter bonuses.
 */
public interface IParameterObject {

    /**
     * Gets the parameters offered by the current object.
     * @return The parameters offered by the current object.
     */
    Iterable<Parameter> getParameters();
}
