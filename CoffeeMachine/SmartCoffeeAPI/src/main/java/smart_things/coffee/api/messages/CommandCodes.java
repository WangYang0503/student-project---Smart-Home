package smart_things.coffee.api.messages;

import java.lang.invoke.WrongMethodTypeException;

/**
 * Enum that collects most of the well-know commands to control the coffee
 * machine with. Saves head and tail of each command and whether it requires
 * parameters or not.
 *
 * @author Jan
 * @version 1.01
 */
public enum CommandCodes {
    BREW_COFFEE("37", "", false), STOP_BREWING("34", "7e", false), CHANGE_STRENGTH("35",
            "7e", true), CHANGE_CUPS("36", "7e", true), CHANGE_HOTPLATE_TIME("3e", "7e", true),
    CHANGE_BREWING_TYPE("3c", "7e", false);

    // Components of a command
    private final String head;
    private final String tail;
    private final boolean needsParam;

    /**
     * Internal constructor to create a command.
     *
     * @param head       The head of the command ("37", "3e" etc.)
     * @param tail       The tail of the command (mostly "7e")
     * @param needsParam True, when the command requires parameters, false otherwise
     */
    private CommandCodes(String head, String tail, boolean needsParam) {
        this.head = head;
        this.tail = tail;
        this.needsParam = needsParam;
    }

    /**
     * Returns the complete command, including head and tail but no parameters.
     * Can only be used on commands, that do not require parameters.
     *
     * @return The complete command
     * @throws WrongMethodTypeException In case of using this method on commands that require
     *                                  parameters
     */
    @Override
    public String toString() {
        if (needsParam) {
            throw new WrongMethodTypeException("This command requires a parameter. Use the build-method instead.");
        }
        return head + tail;
    }

    /**
     * Returns the complete command, including head, tail and parameters.
     *
     * @param param The parameters to use in the command
     * @return The complete command with parameters
     */
    public String build(String param) {
        return head + param + tail;
    }

    /**
     * Returns the head of the command.
     *
     * @return The head of the command
     */
    public String getHead() {
        return head;
    }

    /**
     * Returns the tail of the command.
     *
     * @return The tail of the command
     */
    public String getTail() {
        return tail;
    }

    /**
     * Checks whether the command requires parameters or not.
     *
     * @return True, when the command requires parameters, false otherwise
     */
    public boolean needsParam() {
        return needsParam;
    }
}
