package smart_things.coffee.api.exceptions;

/**
 * Exception for the case that the tank of the coffee machine has not enough
 * water while the coffee machine is still able to boil.
 *
 * @author Jan
 * @version 1.01
 */
public class LowWaterException extends Exception {
    private static final long serialVersionUID = 154349859013611633L;

    public LowWaterException() {
        super();
    }

    public LowWaterException(String message) {
        super(message);
    }

}
