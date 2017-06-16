package smart_things.coffee.api.main;

import smart_things.coffee.api.exceptions.*;
import smart_things.coffee.api.messages.ReplyCodes;

/**
 * Handles the reply codes that were received from the coffee machine. Throws
 * exceptions, if necessary.
 *
 * @author Jan
 * @version 1.01
 */
class ReplyCodeHandler {

    /**
     * Handles default reply codes without considering special coffee machine
     * errors like 'no carafe' etc. Can be used for set-commands, like 'set
     * cups'.
     *
     * @param message The received message to handle
     */
    public void handleDefault(String message) {
        if (message.equals(ReplyCodes.OK.toString())) {
            // No problem, do nothing
        } else if (message.equals(ReplyCodes.ERROR_INVALID_PARAMETER.toString())) {
            // Illegal parameters
            throw new InvalidParameterException("The coffee machine did not accept your parameter in this command.");
        } else if (message.equals(ReplyCodes.ERROR_UNKNOWN_COMMAND.toString())) {
            // Illegal command
            throw new UnknownCommandException("The coffee machine does not recognize this command.");
        } else if (message.equals("")) {
            // Empty message or no message received
            throw new NoReplyException("No reply or empty reply received.");
        } else {
            // Unknown code received
            throw new UnknownCodeException("Unknown error, warning or reply code: " + message);
        }
    }

    /**
     * Handles all reply codes with considering special coffee machine errors
     * like 'no carafe' etc. Can be used for do-commands, like 'brew coffee'.
     *
     * @throws AlreadyBrewingException In case that the coffee machine is already brewing when
     *                                 trying to send the brew command to it
     * @throws NoCarafeException       In case that the coffee machine does not have a (required)
     *                                 carafe at the moment.
     * @throws NoWaterException        In case that the coffee machine has no water anymore
     * @throws LowWaterException       In case that the tank of the coffee machine has not enough
     *                                 water while the coffee machine is still able to boil
     */
    public void handleAll(String message)
            throws AlreadyBrewingException, NoCarafeException, NoWaterException, LowWaterException {
        if (message.equals(ReplyCodes.OK.toString())) {
            // No problem, do nothing
        } else if (message.equals(ReplyCodes.ERROR_ALREADY_BREWING.toString())) {
            throw new AlreadyBrewingException(
                    // Coffee machine is already brewing
                    "The coffee machine is already brewing. You are not able to start this process again.");
        } else if (message.equals(ReplyCodes.ERROR_INVALID_PARAMETER.toString())) {
            // Illegal parameters
            throw new InvalidParameterException("The coffee machine did not accept your parameter in this command.");
        } else if (message.equals(ReplyCodes.ERROR_UNKNOWN_COMMAND.toString())) {
            // Illegal command
            throw new UnknownCommandException("The coffee machine does not recognize this command.");
        } else if (message.equals(ReplyCodes.ERROR_NO_CARAFE.toString())) {
            // Coffee machine has no carafe
            throw new NoCarafeException(
                    "The coffee machine does not have a carafe at the moment. Please deactivate the carafe detection"
                            + " on the coffee machine to continue without a carafe.");
        } else if (message.equals(ReplyCodes.ERROR_NO_WATER.toString())) {
            // Coffee machine has no water anymore
            throw new NoWaterException("The coffee machine does not have enough water anymore.");
        } else if (message.equals(ReplyCodes.WARNING_LOW_WATER.toString())) {
            // Coffee machine claims about less water, but continues
            throw new LowWaterException(
                    "The water level is very low. Brewing is possible, but without promise that there is enough "
                            + "water inside the machine.");
        } else if (message.equals("")) {
            // Empty message or no message received
            throw new NoReplyException("No reply or empty reply received.");
        } else {
            // Unknown code received
            throw new UnknownCodeException("Unknown error, warning or reply code.");
        }
    }
}
