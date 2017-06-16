package smart_things.backend.server.web.notifier;

import java.util.Date;

/**
 * These objects represent notifications which have a ID, a creation date, a type of NotificationType and a
 * message.
 * <p>
 * Created by Jan on 12.02.2017.
 */
public class Notification {
    //Static counter to enumerate notifications and increment their IDs
    private static long next_id_counter = 0;

    //Unique ID of the notification
    private long id = 0;
    //Creating date of the notification
    private Date date = null;
    //Type of the notification
    private NotificationType type = null;
    //Message of the notification
    private String message = null;

    /**
     * Creates a new notification by a given notification type and a message. The creation date is set to the current
     * date and time.
     *
     * @param type    The type of the notification
     * @param message The message of the notification
     */
    public Notification(NotificationType type, String message) {
        //Set attributes
        this.id = next_id_counter;
        this.type = type;
        this.message = message;
        this.date = new Date();

        //Increment ID counter, so the next notification will have a higher ID
        next_id_counter++;
    }

    /**
     * Creates a new notification by a given creation date, a notification type and a message.
     *
     * @param date    Creation date of the notification
     * @param type    The type of the notification
     * @param message The message of the notification
     */
    public Notification(Date date, NotificationType type, String message) {
        //Set attributes
        this.id = next_id_counter;
        this.date = date;
        this.type = type;
        this.message = message;

        //Increment ID counter, so the next notification will have a higher ID
        next_id_counter++;
    }

    /**
     * Returns the unique id of this notification.
     *
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the creation date of this notification.
     *
     * @return The date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of this notification.
     *
     * @param date The date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the type of this notification.
     *
     * @return The type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Sets the type of this notification.
     *
     * @param type The type to set
     */
    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * Returns the message of this notification.
     *
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of this notification.
     *
     * @param message The message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
