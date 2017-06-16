package smart_things.backend.server.web.handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.main.MultiLogger;
import smart_things.backend.server.rules.RuleWorker;
import smart_things.backend.server.web.notifier.Notification;
import smart_things.backend.server.web.notifier.UserNotifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * This implementation of a http request handler is called when there are incoming "/poll" requests with which the
 * corresponding client wants to poll the server for new notifications. This uses long polling, a request for fetching
 * new notifications will be answered when there are new notifications; otherwise the request will be kept open and
 * time out after plenty seconds, in the case, that there was no new notification in this time. After the last request
 * has ended the client has the possibility to start a new long polling request. In order to pull new notifications
 * it is necessary that the parameter "last_id" (id of newest received notification) is passed as post parameter.
 * Uses the AbstractHandler that implements some basic methods for handling http requests as framework superclass.
 * <p>
 * Created by Jan on 25.01.2017.
 */
public class PollHandler extends AbstractHandler {

    //Number of seconds after which a open request gets an empty response
    private static final int REQUEST_TIMEOUT = 30;

    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public PollHandler(DBManager dbManager, RuleWorker ruleWorker) {
        super(dbManager, ruleWorker);
    }

    /**
     * Handles the incoming request. If invalid parameters are passed or there are other problems, the handler will
     * reply the request by sending an error message. If everything was successful, a success response will be sent.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        //Run handling of the poll request in a new thread in order to allow multiple requests at the same time
        new Thread(() -> {
            try {
                //Fetch the post parameters from the incoming request
                Map<String, Object> params =
                        (Map<String, Object>) httpExchange.getAttribute("parameters");

                //Check for the parameter whether it was passed. If it exists, continue, otherwise send an error
                //message and return
                if (!params.containsKey("last_id")) {
                    sendError(httpExchange, "No last id given.");
                    return;
                }

                //Get the id of the newest received notification from the parameter map
                int lastID = Integer.parseInt((String) params.get("last_id"));

                //Create a empty list to store the notifications in that have to be sent back to the client
                ArrayList<Notification> list = null;

                //Check for new notifications, REQUEST_TIMEOUT seconds long (one check per second)
                for (int i = 0; i < REQUEST_TIMEOUT; i++) {
                    //Retrieve notification list including all notifications that are newer than lastID
                    list = UserNotifier.getNotificationList(lastID);
                    //Check for valid notification list (contains at least one element) and prepare reply
                    if ((list != null) && (list.size() > 0)) {
                        //Create new JSONObject to prepare the reply
                        JSONObject jsonObject = new JSONObject();
                        //Create new JSONArray in which all new notifications will be stored
                        JSONArray notificationArray = new JSONArray();
                        //Stores the notification as JSONObject that is currently considered
                        JSONObject thisNotification = null;

                        //Iterate over all new notifications
                        for (Notification notification : list) {
                            //Create a JSONObject from the current notification
                            thisNotification = new JSONObject();
                            thisNotification.put("id", notification.getId());
                            thisNotification.put("date", notification.getDate().getTime());
                            thisNotification.put("type", notification.getType().toString());
                            thisNotification.put("message", notification.getMessage());

                            //Add the new notification JSONObject to the array
                            notificationArray.add(thisNotification);
                        }
                        //Add the JSONArray of all notifications to the reply and prepare it
                        jsonObject.put("notifications", notificationArray);
                        jsonObject.put("error", false);

                        //Send JSON reply to the requested client and return (request is done)
                        writeResponse(httpExchange, jsonObject);
                        return;
                    }
                    //Wait one second until the next notification check
                    Thread.sleep(1000);
                }
                //In this period no new notification could be found, so timeout with an empty success reply
                sendSuccess(httpExchange);
            } catch (IOException e) {
                MultiLogger.severe("IOException while responding to poll request: " + e.getMessage());
            } catch (InterruptedException e) {
                MultiLogger.severe("InterruptedException while handling web server request: " + e.getMessage());
            }
        }).start(); //Start new thread
    }
}