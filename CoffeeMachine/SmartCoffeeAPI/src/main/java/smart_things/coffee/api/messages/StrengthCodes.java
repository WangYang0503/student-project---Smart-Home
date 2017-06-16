package smart_things.coffee.api.messages;

/**
 * Contains strength codes representing the current strength level of the coffee
 * machine.
 *
 * @author Jan
 * @version 1.01
 */
public enum StrengthCodes {

    // List of strength codes
    UNKNOWN("", "Unknown"), WEAK("00", "Weak"), MEDIUM("01", "Medium"), STRONG("02", "Strong");

    // Code that is corresponding to the strength
    private String code;
    // Name that is corresponding to the strength
    private String name;

    /**
     * Private constructor for strength codes.
     *
     * @param code The code that is representing the strength
     * @param name The name that is representing the strength
     */
    private StrengthCodes(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the code for this strength.
     *
     * @return The code of the strength
     */
    @Override
    public String toString() {
        return this.code;
    }

    /**
     * Returns the human readable name for this strength.
     *
     * @return The name of the strength.
     */
    public String getName() {
        return name;
    }
}
