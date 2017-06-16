package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that no answer is recieved from the coffee machine
 * after sending a command to it.
 *
 * @author Jan
 * @version 1.01
 */
public class NoReplyException extends RuntimeException {
    private static final long serialVersionUID = 154349859013611633L;

    public NoReplyException() {
        super();
    }

    public NoReplyException(String message) {
        super(message);
    }

}
