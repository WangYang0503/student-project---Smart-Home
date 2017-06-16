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
 * This implementation of a http request handler is called when there are incoming "/switch" requests with which the
 * user wants to turn on/off a rule from his Smart Home. In order to switch a rule's state it is necessary that
 * the parameter "name" (name of the rule as string) is passed as post parameter and that a rule with this name
 * exists.
 * Uses the AbstractHandler that implements some basic methods for handling http requests as framework superclass.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class SwitchRuleHandler extends AbstractHandler {
    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public SwitchRuleHandler(DBManager dbManager, RuleWorker ruleWorker) {
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

        //Check for the parameter whether it was passed. If it exists, continue, otherwise send an error message and
        //return
        if (!params.containsKey("name")) {
            sendError(httpExchange, "No rule name given.");
            return;
        }

        //Get the rule name from the parameter map
        String ruleName = (String) params.get("name");

        try {
            //Check if the rule exists and reply with an error message otherwise
            if (!dbManager.ruleExists(ruleName)) {
                sendError(httpExchange, "The rule \"" + ruleName + "\" does not exist!");
                return;
            }

            //Retrieve the rule from the database
            Rule rule = dbManager.getRule(ruleName);

            //Toggle the rule state
            if (rule.isEnabled()) {
                dbManager.disableRule(ruleName);
            } else {
                dbManager.enableRule(ruleName);
            }
        } catch (SQLException e) {
            MultiLogger.severe("SQLException at web server: " + e.getMessage());
            sendError(httpExchange, "Internal database error.");
            return;
        }

        //Everything okay, send a success response
        sendSuccess(httpExchange);
    }
}
