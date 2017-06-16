package smart_things.coffee.api.messages;

/**
 * Contains status activity types that represent what the coffee machine is
 * currently doing. These status types are part of the whole status codes that
 * the coffee machine sends with its polling messages.
 *
 * @author Jan
 */
public enum StatusActivityTypes {
    UNKOWN, READY, BREWING, GRINDING, DONE
}
