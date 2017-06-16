package smart_things.backend.server.web.notifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Works like a logger that sends all logs in the form of notificationsto the web interface which is shown to the
 * user/client.
 * <p>
 * Created by Jan on 12.02.2017.
 */
public class UserNotifier {
    //Time (in milliseconds) after which new notifications will not be sent anymore
    private static final int KEEP_TIME = 1000 * 60;
    //Current queue of all relevant notifications
    private static volatile LinkedList<Notification> notificationQueue;

    static {
        //Create new notification queue
        notificationQueue = new LinkedList<Notification>();
    }

    /**
     * Pushes a new notification to the top of the notification queue in order to be pollable through the client.
     *
     * @param notification The notification to be pushed
     */
    public static void pushNotification(Notification notification) {
        //Check whether the notification is still relevant for polling
        if (isTooOld(notification)) {
            //Too old (older than KEEP_TIME), so do not push it
            return;
        }
        //Add notification to queue
        notificationQueue.add(notification);
    }

    /**
     * Retrieves a list containing all notifications that are newer than the notification with id lastID,
     * but not older than KEEP_TIME. It is the list of notifications that can be replied to a poll request.
     *
     * @param lastID The ID of the latest received notification
     * @return An ArrayList containing the new notifications
     */
    public static ArrayList<Notification> getNotificationList(int lastID) {

        //Create empty ArrayList
        ArrayList<Notification> notificationList = new ArrayList<Notification>();

        //Stores the notification which is currently considered
        Notification notification = null;

        //Iterate over all available notifications in notificationQueue
        for (int i = 0; i < notificationQueue.size(); i++) {
            //Retrieve current notification
            notification = notificationQueue.get(i);
            //Check whether the current notification is older than KEEP_TIME
            if (isTooOld(notification)) {
                //Too old, remove from queue
                notificationQueue.remove(i);
                i--;
            } else {
                //Not too old, use the id to check whether this notification has already been sent
                if (notification.getId() <= lastID) {
                    continue;
                }
                //Add notification to final list
                notificationList.add(notification);
            }
        }
        //Return null in case that no matching notification has been found
        if (notificationList.isEmpty()) {
            return null;
        }
        //Return whole notification list
        return notificationList;
    }

    /**
     * Checks whether a notification has become too old (older than KEEP_TIME) for getting polled through a client.
     *
     * @param notification The notification to check
     * @return True, if the notification is too old to be pulled, false otherwise.
     */
    private static boolean isTooOld(Notification notification) {
        //Retrieve creation time of notification
        long notificationTime = notification.getDate().getTime();
        //Calculate current time
        long nowTime = new Date().getTime();
        //Check period of time between creation and the current time
        return nowTime - notificationTime > KEEP_TIME;
    }
}
