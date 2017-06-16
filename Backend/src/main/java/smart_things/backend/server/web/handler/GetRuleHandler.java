package smart_things.backend.server.web.handler;

import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.main.MultiLogger;
import smart_things.backend.server.rules.Rule;
import smart_things.backend.server.rules.RuleWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This implementation of a http request handler is called when there are incoming "/get" requests with which the
 * user wants to retrieve all rules and possibly further information from his Smart Home. There are no parameters that
 * have to be passed. Uses the AbstractHandler that implements some basic methods for handling http requests as
 * framework superclass.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class GetRuleHandler extends AbstractHandler {
    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public GetRuleHandler(DBManager dbManager, RuleWorker ruleWorker) {
        super(dbManager, ruleWorker);
    }

    /**
     * Handles the incoming request. If there are any problems, the handler will reply the request by sending an
     * error message. If everything was successful, a success response will be sent.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //Create a empty list of all rules
        List<Rule> ruleList = null;

        try {
            //Retrieve all rules from the database
            ruleList = dbManager.getAllRules();
        } catch (SQLException e) {
            MultiLogger.severe("SQLException at web server: " + e.getMessage());
            sendError(httpExchange, "Internal database error.");
            return;
        }

        //Create a JSON object that will be returned finally
        JSONObject mainJSONObject = new JSONObject();
        //Create a JSON array in which the rules will be stored and which will be attached to the json object
        JSONArray ruleJSONArray = new JSONArray();

        //JSON object that is used to store every single rule
        JSONObject ruleJSONObject;

        //Iterate over all rules in the list
        for (Rule rule : ruleList) {
            //Create new JSON object
            ruleJSONObject = new JSONObject();

            //Store the current rule in the JSON object
            ruleJSONObject.put("name", rule.getName());
            ruleJSONObject.put("trigger", rule.getTriggerClass());
            ruleJSONObject.put("action", rule.getActionMethod());
            ruleJSONObject.put("timestamp", rule.getTime().getTime());
            ruleJSONObject.put("enabled", rule.isEnabled());

            //Add the rule JSON object to the array
            ruleJSONArray.add(ruleJSONObject);
        }

        //Add the array to the main (parent) JSON object together with a success response
        mainJSONObject.put("rules", ruleJSONArray);
        mainJSONObject.put("error", false);

        //Send the main JSON object as response
        writeResponse(httpExchange, mainJSONObject);
    }
}
