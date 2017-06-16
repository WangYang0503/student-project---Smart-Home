package smart_things.backend.server.web.handler;

import com.sun.net.httpserver.HttpExchange;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.main.MultiLogger;
import smart_things.backend.server.rules.Rule;
import smart_things.backend.server.rules.RuleWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * This implementation of a http request handler is called when there are incoming "/edit" requests with which the
 * user wants to edit a existing rule. In order to edit a rule it is necessary that the parameters "name"
 * (name of the field that shall be edited as string), "pk" (primary key value (name) of the rule that shall be edited)
 * and "value" (the new value of the field) are passed as post parameter and that a rule with this primary key name
 * exists. Uses the AbstractHandler that implements some basic methods for handling http requests as framework
 * superclass.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class EditRuleHandler extends AbstractHandler {
    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public EditRuleHandler(DBManager dbManager, RuleWorker ruleWorker) {
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
    public void handle(HttpExchange httpExchange) throws IOException {
        //Fetch the post parameters from the incoming request
        Map<String, Object> params = (Map<String, Object>) httpExchange.getAttribute("parameters");

        //Check for all necessary parameters whether they were passed. If they exist, continue, otherwise send an error
        //message and return
        if (!params.containsKey("name")) {
            sendError(httpExchange, "No field name to edit given.");
            return;
        } else if (!params.containsKey("pk")) {
            sendError(httpExchange, "No rule name (primary key) given.");
            return;
        } else if (!params.containsKey("value")) {
            sendError(httpExchange, "No new value given.");
            return;
        }

        //Store parameters in local variables
        String fieldName = (String) params.get("name");
        String ruleName = (String) params.get("pk");
        String newValue = (String) params.get("value");

        //If now new value was passed, interpret this as empty string
        if (newValue == null) {
            newValue = "";
        }

        try {
            //Check if the rule exists and reply with an error message otherwise
            if (!dbManager.ruleExists(ruleName)) {
                sendError(httpExchange, "The rule \"" + ruleName + "\" does not exist!");
                return;
            }

            //Retrieve the rule from the database
            Rule rule = dbManager.getRule(ruleName);

            //Distinction of cases regarding the field to edit
            switch (fieldName) {
                case "name":
                    //Name (primary key) of the rule shall be edited, check first whether the new name is free and
                    //reply with an error message otherwise
                    if (dbManager.ruleExists(newValue)) {
                        sendError(httpExchange, "A rule named \"" + fieldName + "\" already exists! The rule name" +
                                " has to be unique.");
                        return;
                    }

                    //Update the rule primary key and send success message
                    dbManager.updateRuleID(rule, newValue);
                    sendSuccess(httpExchange);
                    return;
                case "trigger":
                    //Update the trigger class name in the local rule
                    rule.setTriggerClass(newValue);
                    break;
                case "action":
                    //Update the action method name in the local rule
                    rule.setActionMethod(newValue);
                    break;
                default:
                    //Field is unknown, send an error message
                    sendError(httpExchange, "Unknown field name \"" + fieldName + "\".");
                    return;
            }

            //Update the rule in the database
            dbManager.updateRule(rule);

        } catch (SQLException e) {
            MultiLogger.severe("SQLException at web server: " + e.getMessage());
            sendError(httpExchange, "Internal database error.");
            return;
        }
        //Everything okay, send a success response
        sendSuccess(httpExchange);
    }
}
