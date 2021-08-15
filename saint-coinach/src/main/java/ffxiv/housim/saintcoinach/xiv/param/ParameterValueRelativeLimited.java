package ffxiv.housim.saintcoinach.xiv.param;

import lombok.Getter;

/**
 * Class representing a relative parameter bonus that can only grant up to a certain amount.
 */
public class ParameterValueRelativeLimited extends ParameterValueRelative {
    /**
     * The maximum bonus granted.
     */
    @Getter
    private int maximum;

    /**
     * Initializes a new instance of the {@link ParameterValueRelativeLimited} class.
     *
     * @param type {@link ParameterType} of the bonus.
     * @param amount Bonus amount.
     * @param maximum Maximum bonus granted.
     * @param index Index of the bonus.
     */
    public ParameterValueRelativeLimited(ParameterType type, double amount, int maximum, int index) {
        super(type, amount, index);
        this.maximum = maximum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (amount > 0 && type != ParameterType.Primary && type != ParameterType.Base)
            sb.append('+');
        sb.append(amount);

        sb.append(" (max. ").append(maximum).append(")");

        return sb.toString();
    }
}
