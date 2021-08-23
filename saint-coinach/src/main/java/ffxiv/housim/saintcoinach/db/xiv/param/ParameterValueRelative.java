package ffxiv.housim.saintcoinach.db.xiv.param;

import lombok.Getter;

/**
 * Class representing a relative parameter bonus.
 */
public class ParameterValueRelative extends ParameterValue {
    /**
     * The current relative bonus.
     */
    @Getter
    protected double amount;

    /**
     * Initializes a new instance of the {@link ParameterValueRelative} class.
     *
     * @param type {@link ParameterType} of the bonus.
     * @param amount Relative bonus.
     * @param index Index of the bonus.
     */
    public ParameterValueRelative(ParameterType type, double amount, int index) {
        super(type, index);
        this.amount = amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (amount > 0 && type != ParameterType.Primary && type != ParameterType.Base)
            sb.append('+');
        sb.append(amount);

        return sb.toString();
    }
}
