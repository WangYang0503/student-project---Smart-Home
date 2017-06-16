package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that the coffee machine does not recognize the command
 * that was send to it.
 *
 * @author Jan
 * @version 1.01
 */
public class UnknownCommandException extends RuntimeException {

    private static final long serialVersionUID = 2501021860837203434L;

    public UnknownCommandException() {
        super();
    }

    public UnknownCommandException(String message) {
        super(message);
    }

}
