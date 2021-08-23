package ffxiv.housim.saintcoinach.db.xiv.param;

import lombok.Getter;

/**
 * Class representing a fixed-value parameter bonus.
 */
public class ParameterValueFixed extends ParameterValue {
    /**
     * The current bonus amount.
     */
    @Getter
    private double amount;

    /**
     * Initializes a new instance of the {@link ParameterValueFixed} class.
     *
     * @param type {@link ParameterType} of the bonus.
     * @param amount Bonus amount.
     * @param index Parameter index.
     */
    public ParameterValueFixed(ParameterType type, double amount, int index) {
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
