package smart_things.coffee.api.messages;

/**
 * Contains common reply codes that the coffee machine sends after commands.
 *
 * @author Jan
 * @version 1.01
 */
public enum ReplyCodes {

    //List of reply codes
    OK("03007e"), ERROR_ALREADY_BREWING("03017e"), UNKNOWN_1("03027e"), UNKNOWN_2("03037e"), ERROR_INVALID_PARAMETER(
            "03047e"), ERROR_NO_CARAFE("03057e"), ERROR_NO_WATER("03067e"), WARNING_LOW_WATER("03077e"),
    ERROR_UNKNOWN_COMMAND("03697e");

    // Code that will be sent to trigger this reply
    private String code;

    /**
     * Private constructor for reply codes.
     *
     * @param code The code that will be sent
     */
    private ReplyCodes(String code) {
        this.code = code;
    }

    /**
     * Returns the code of this reply.
     *
     * @return The code of the reply
     */
    @Override
    public String toString() {
        return this.code;
    }
}
