package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that the coffee machine is already brewing when trying
 * to send the brew command to it.
 *
 * @author Jan
 * @version 1.01
 */
public class AlreadyBrewingException extends Exception {
    private static final long serialVersionUID = 154349859013611633L;

    public AlreadyBrewingException() {
        super();
    }

    public AlreadyBrewingException(String message) {
        super(message);
    }

}
