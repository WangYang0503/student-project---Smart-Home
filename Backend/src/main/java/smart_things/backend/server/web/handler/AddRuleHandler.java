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
 * This implementation of a http request handler is called when there are incoming "/add" requests with which the user
 * wants to add a new rule to his Smart Home. In order to add a new rule it is necessary that the parameters "name"
 * (name of the new rule as string), "trigger" (trigger event class name as string), "action" (action method name as
 * string) and "enabled" (state of the rule as boolean) are passed as post parameters and that the rule name is unique.
 * Uses the AbstractHandler that implements some basic methods for handling http requests as framework superclass.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class AddRuleHandler extends AbstractHandler {
    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public AddRuleHandler(DBManager dbManager, RuleWorker ruleWorker) {
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
        Map<String, Object> params =
                (Map<String, Object>) httpExchange.getAttribute("parameters");

        //Check for all necessary parameters whether they were passed. If they exist, continue, otherwise send an error
        //message and return
        if (!params.containsKey("name")) {
            sendError(httpExchange, "No rule name given.");
            return;
        } else if (!params.containsKey("trigger")) {
            sendError(httpExchange, "No trigger event class name given.");
            return;
        } else if (!params.containsKey("action")) {
            sendError(httpExchange, "No action method name given.");
            return;
        } else if (!params.containsKey("enabled")) {
            sendError(httpExchange, "No rule state given.");
            return;
        }

        //Create a new rule object correspondig to the given parameters
        Rule newRule = new Rule();
        newRule.setName((String) params.get("name"));
        newRule.setTriggerClass((String) params.get("trigger"));
        newRule.setActionMethod((String) params.get("action"));
        newRule.setEnabled(Boolean.parseBoolean((String) params.get("enabled")));
        //Refresh the creation time of the rule
        newRule.refreshTime();

        try {
            //Check if the rule already exists and reply with an error message in this case
            if (dbManager.ruleExists(newRule.getName())) {
                sendError(httpExchange, "The rule \"" + newRule.getName() + "\" already exists!");
                return;
            }

            //Add the rule to the database
            dbManager.addRule(newRule);

        } catch (SQLException e) {
            MultiLogger.severe("SQLException at web server: " + e.getMessage());
            sendError(httpExchange, "Internal database error.");
            return;
        }

        //Everything okay, send a success response
        sendSuccess(httpExchange);
    }
}
