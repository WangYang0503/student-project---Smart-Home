package smart_things.backend.server.main;

import org.apache.avro.specific.SpecificRecordBase;
import smart_things.backend.server.web.notifier.Notification;
import smart_things.backend.server.web.notifier.NotificationType;
import smart_things.backend.server.web.notifier.UserNotifier;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logger which is able to write logs into a logfile and/or to push them on the notification queue in order to send
 * them as notifications to polling clients.
 * <p>
 * Created by Jan on 29.01.2017.
 */
public class MultiLogger {
    //Relative root folder to store the log files in
    private static final String LOG_ROOT = "WebUI/log";
    //Name of the log file to use
    private static final String LOG_FILE = "log.txt";

    //File logger
    private static Logger logger = null;
    //File handler for the file logger
    private static FileHandler handler = null;

    static {
        //Create the file logger
        logger = Logger.getLogger("BackendLogger");

        //Configure the file logger
        try {
            //Create file handler to manage the log file and add it to the logger
            handler = new FileHandler(LOG_ROOT + "/" + LOG_FILE);
            logger.addHandler(handler);

            //Set Formatter of the logger
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Writes a message with severe priority level to the logfile.
     *
     * @param message The message to log
     */
    public static void severe(String message) {
        logger.severe(message);
    }

    /**
     * Writes a message with severe priority level to the logfile and pushes a corresponding notification to the
     * notification queue in order to notify polling clients.
     *
     * @param message The message to log and push
     */
    public static void severeNotify(String message) {
        //Log to file
        severe(message);
        //Push notification
        UserNotifier.pushNotification(new Notification(NotificationType.ERROR, message));

    }

    /**
     * Logs a warning message to the logfile.
     *
     * @param message The message to log
     */
    public static void warning(String message) {
        logger.warning(message);
    }

    /**
     * Writes a warning message to the logfile and pushes a corresponding notification to the notification queue in
     * order to notify polling clients.
     *
     * @param message The message to log and push
     */
    public static void warningNotify(String message) {
        //Log to file
        warning(message);
        //Push notification
        UserNotifier.pushNotification(new Notification(NotificationType.WARNING, message));
    }

    /**
     * Writes an information message to the logfile.
     *
     * @param message The message to log
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs a information message to the logfile and pushes a corresponding notification to the notification queue in
     * order to notify polling clients.
     *
     * @param message The message to log and push
     */
    public static void infoNotify(String message) {
        //Log to file
        info(message);
        //Push notification
        UserNotifier.pushNotification(new Notification(NotificationType.INFO, message));
    }

    /**
     * Writes details about an incoming event to the logfile.
     *
     * @param event The incoming event to log
     */
    public static void eventIn(SpecificRecordBase event) {
        logger.info("---> Incoming KAA event: " + event.getClass().getCanonicalName());
    }

    /**
     * Logs details about an incoming event to the logfile and pushes a corresponding notification to the notification
     * queue in order to notify polling clients.
     *
     * @param event The incoming event to log and push
     */
    public static void eventInNotify(SpecificRecordBase event) {
        //Log to file
        eventIn(event);
        //Push notification
        UserNotifier.pushNotification(new Notification(NotificationType.EVENT_IN, event.getClass().getSimpleName()));
    }

    /**
     * Writes details about an outgoing event to the logfile.
     *
     * @param event The outgoing event to log
     */
    public static void eventOut(SpecificRecordBase event) {
        logger.info("<--- Outgoing KAA event: " + event.getClass().getCanonicalName());
    }

    /**
     * Logs details about an outgoing event to the logfile and pushes a corresponding notification to the notification
     * queue in order to notify polling clients.
     *
     * @param event The outgoing event to log and push
     */
    public static void eventOutNotify(SpecificRecordBase event) {
        //Log to file
        eventOut(event);
        //Push notification
        UserNotifier.pushNotification(new Notification(NotificationType.EVENT_OUT, event.getClass().getSimpleName()));
    }
}