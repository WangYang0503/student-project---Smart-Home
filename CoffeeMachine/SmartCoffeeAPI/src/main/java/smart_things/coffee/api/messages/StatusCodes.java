package smart_things.coffee.api.messages;

/**
 * Contains common status codes representing the hardware states in which the
 * coffee machine can be.
 *
 * @author Jan
 * @version 1.01
 */
public enum StatusCodes {

    //@format:off
    // List of hardware status codes
    UNKNOWN(""),
    READY_FILTER_NO_CARAFE("04", StatusActivityTypes.READY, true, false),
    READY_FILTER_CARAFE("05", StatusActivityTypes.READY, true, true),
    READY_GRINDER_NO_CARAFE("06", StatusActivityTypes.READY, false, false),
    READY_GRINDER_CARAFE("07", StatusActivityTypes.READY, false, true),
    GRINDING_GRINDER_NO_CARAFE("0a", StatusActivityTypes.GRINDING, false, false),
    GRINDING_GRINDER_CARAFE("0b", StatusActivityTypes.GRINDING, false, true),
    BREWING_FILTER_NO_CARAFE("10", StatusActivityTypes.BREWING, true, false),
    BREWING_GRINDER_NO_CARAFE("12", StatusActivityTypes.BREWING, false, false),
    BREWING_FILTER_CARAFE("51", StatusActivityTypes.BREWING, true, true),
    BREWING_GRINDER_CARAFE("53", StatusActivityTypes.BREWING, false, true),
    DONE_FILTER_NO_CARAFE("44", StatusActivityTypes.DONE, true, false),
    DONE_GRINDER_NO_CARAFE("46", StatusActivityTypes.DONE, false, false),
    DONE_FILTER_CARAFE("45", StatusActivityTypes.DONE, true, true),
    DONE_GRINDER_CARAFE("47", StatusActivityTypes.DONE, false, true);
    //@format:on

    // Code that is corresponding to the state
    private String code;
    private StatusActivityTypes status = StatusActivityTypes.UNKOWN;
    private boolean filter = false;
    private boolean carafe = false;
    // The following properties determine about which of these attributes the
    // status makes an statement
    private boolean statusSpecified = false;
    private boolean brewingTypeSpecified = false;
    private boolean carafeStatusSpecified = false;

    /**
     * Private constructor for status codes.
     *
     * @param code The code that is representing the state
     */
    private StatusCodes(String code) {
        this.code = code;
    }

    /**
     * Private constructor for status codes with status type.
     *
     * @param code   The code that is representing the state
     * @param status The activity status of the coffee machine
     */
    private StatusCodes(String code, StatusActivityTypes status) {
        this.code = code;
        this.status = status;
        this.statusSpecified = true;
    }

    /**
     * Private constructor for status codes with status type and filter
     * property.
     *
     * @param code   The code that is representing the state
     * @param status The activity status of the coffee machine
     * @param filter True, when the coffee machine uses currently the filter for brewing; false,
     *               when the coffee machine uses the grinder
     */
    private StatusCodes(String code, StatusActivityTypes status, boolean filter) {
        this.code = code;
        this.status = status;
        this.filter = filter;
        this.statusSpecified = true;
        this.brewingTypeSpecified = true;
    }

    /**
     * Private constructor for status codes with status type, filter and carafe
     * properties.
     *
     * @param code   The code that is representing the state
     * @param status The activity status of the coffee machine
     * @param filter True, when the coffee machine uses the filter for brewing in this status;
     *               false, when the coffee machine uses the grinder
     * @param carafe True, when the carafe is available in the coffee machine in this status; false,
     *               when the carafe is not available
     */
    private StatusCodes(String code, StatusActivityTypes status, boolean filter, boolean carafe) {
        this.code = code;
        this.status = status;
        this.filter = filter;
        this.carafe = carafe;
        this.statusSpecified = true;
        this.brewingTypeSpecified = true;
        this.carafeStatusSpecified = true;
    }

    /**
     * Returns the code of this status.
     *
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the activity status of this status combination.
     *
     * @return The activity status
     */
    public StatusActivityTypes getStatus() {
        return status;
    }

    /**
     * Returns the brewing type of this status combination.
     *
     * @return True, when the status implies that the coffee machine uses the filter for brewing;
     * false, when the coffee machine uses the grinder
     */
    public boolean isFilter() {
        return filter;
    }

    /**
     * Returns the carafe status of this status combination.
     *
     * @return True, when the status implies that there is a carafe available in the coffee machine;
     * false, when there is no carafe available
     */
    public boolean isCarafe() {
        return carafe;
    }

    /**
     * Checks whether this status makes a statement about the activity status.
     *
     * @return True, when the status makes a statement; false otherwise
     */
    public boolean isStatusSpecified() {
        return statusSpecified;
    }

    /**
     * Checks whether this status makes a statement about the brewing type.
     *
     * @return True, when the status makes a statement; false otherwise
     */
    public boolean isBrewingTypeSpecified() {
        return brewingTypeSpecified;
    }

    /**
     * Checks whether this status makes a statement about the carafe status.
     *
     * @return True, when the status makes a statement; false otherwise
     */
    public boolean isCarafeStatusSpecified() {
        return carafeStatusSpecified;
    }

    /**
     * Returns the code for this status.
     *
     * @return The code of the status
     */
    @Override
    public String toString() {
        return this.code;
    }
}
