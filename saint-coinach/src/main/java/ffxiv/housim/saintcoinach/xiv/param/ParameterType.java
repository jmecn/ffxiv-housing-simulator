package ffxiv.housim.saintcoinach.xiv.param;

/**
 * Enumeration of known types of parameter bonuses.
 */
public enum ParameterType {
    /**
     * Value indicating the bonus is always present on an object.
     */
    Base,

    /**
     * Value indicating the bonus is a primary parameter and always present on an object.
     */
    Primary,

    /**
     * Value indicating the bonus is only valid when set conditions are fulfilled.
     */
    SetBonus,

    /**
     * Value indicating the bonus is only valid while a Grand Company sanction is active.
     */
    Sanction,

    /**
     * Value indicating the bonus is only valid when an object is high-quality.
     */
    Hq,

    /**
     * Value indicating the bonus is variable and can differ between instance.
     */
    Variable,

    /**
     * Value indicating the bonus is only valid when set and level conditions are fulfilled.
     */
    SetBonusCapped,

    /**
     * Value indicating the bonus is only valid inside Eureka.
     */
    EurekaEffect,

}
