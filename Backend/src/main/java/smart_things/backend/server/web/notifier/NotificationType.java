package smart_things.backend.server.web.notifier;

/**
 * Marks different types for notifications:
 * EVENT_IN: Notification about incoming event that gets handled
 * EVENT_OUT: Notification about outgoing event that has been sent
 * ERROR: Notification about an error that has been occurred
 * WARNING: Notification about an warning that has been occurred
 * INFO: Notification about a general information or message
 * <p>
 * Created by Jan on 12.02.2017.
 */
public enum NotificationType {
    EVENT_IN, EVENT_OUT, ERROR, WARNING, INFO
}
