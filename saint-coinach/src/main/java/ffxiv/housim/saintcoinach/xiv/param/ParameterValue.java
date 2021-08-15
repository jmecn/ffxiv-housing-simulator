package ffxiv.housim.saintcoinach.xiv.param;

import lombok.Getter;

/**
 * Base class for representing a parameter bonus.
 */
public abstract class ParameterValue {
    @Getter
    protected ParameterType type;
    @Getter
    private int index;

    protected ParameterValue(ParameterType type, int index) {
        this.type = type;
        this.index = index;
    }
}
