package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that a received code is unknown.
 *
 * @author Jan
 * @version 1.01
 */
public class UnknownCodeException extends RuntimeException {
    private static final long serialVersionUID = 154349859013611633L;

    public UnknownCodeException() {
        super();
    }

    public UnknownCodeException(String message) {
        super(message);
    }

}
