package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that the coffee machine does not have a (required)
 * carafe at the moment.
 *
 * @author Jan
 */
public class NoCarafeException extends Exception {
    private static final long serialVersionUID = 154349859013611633L;

    public NoCarafeException() {
        super();
    }

    public NoCarafeException(String message) {
        super(message);
    }

}
