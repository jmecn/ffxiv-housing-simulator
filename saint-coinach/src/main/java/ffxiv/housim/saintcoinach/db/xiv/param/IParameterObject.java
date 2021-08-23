package ffxiv.housim.saintcoinach.db.xiv.param;

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
