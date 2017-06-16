package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that the coffee machine does not understand a
 * parameter that was send to it in a command.
 *
 * @author Jan
 * @version 1.01
 */
public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = 154349859013611633L;

    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(String message) {
        super(message);
    }

}
