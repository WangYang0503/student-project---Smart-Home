package smart_things.coffee.api.messages;

/**
 * Contains water level codes representing the water level in the tank of the
 * coffee machine.
 *
 * @author Jan
 * @version 1.01
 */
public enum WaterLevelCodes {

    // List of water level codes
    UNKNOWN("", "Unknown"), EMPTY("00", "Empty"), LESS("01", "Less"), LESS2("11", "Less"), HALF("12", "Half"),
    FULL("13", "Full");

    // Code that is corresponding to the water level
    private String code;
    // Name that is corresponding to the water level
    private String name;

    /**
     * Private constructor for water level codes.
     *
     * @param code The code that is representing the water level
     * @param name The name that is representing the water level
     */
    private WaterLevelCodes(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the code for this water level.
     *
     * @return The code of the water level
     */
    @Override
    public String toString() {
        return this.code;
    }

    /**
     * Returns the human readable name for this water level.
     *
     * @return The name of the water level
     */
    public String getName() {
        return name;
    }
}
