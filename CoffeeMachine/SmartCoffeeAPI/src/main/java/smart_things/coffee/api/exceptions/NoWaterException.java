package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that the coffee machine has no water anymore.
 *
 * @author Jan
 * @version 1.01
 */
public class NoWaterException extends Exception {
    private static final long serialVersionUID = 154349859013611633L;

    public NoWaterException() {
        super();
    }

    public NoWaterException(String message) {
        super(message);
    }

}
