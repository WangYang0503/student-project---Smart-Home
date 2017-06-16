package smart_things.backend.server.web.handler;

import com.sun.net.httpserver.HttpExchange;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.rules.RuleWorker;

import java.io.File;
import java.io.IOException;

/**
 * This implementation of a http request handler is called when there are incoming default http GET requests with
 * which the browser fetches files from the server. A white list (from the AbstractHandler) is used to check if the
 * requested file is allowed to be delivered. If the file is not allowed to be delivered or nor other file is
 * requested, the index (main) HTML file of the admin web ui is delivered.
 * Uses the AbstractHandler that implements some basic methods for handling http requests as framework superclass.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class MainHandler extends AbstractHandler {
    //Main HTML file of the admin web ui
    private final File indexFile = new File(rootPath + "index.html");

    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public MainHandler(DBManager dbManager, RuleWorker ruleWorker) {
        super(dbManager, ruleWorker);
    }

    /**
     * Handles the incoming request. If everything was successful, the requested file will be delivered, as long as
     * its location is part of the white path list which contains all paths from which files are allowed to be
     * delivered. If the path is not on the list, the main HTML file will be delivered.
     *
     * @param httpExchange The HttpExchange object that contains further information about the incoming request
     * @throws IOException In case of an unexpected stream problem while sending the reply
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //Fetch the requested url
        String requestString = httpExchange.getRequestURI().toASCIIString();

        //Check if the requested file's path is part of the path white list
        for (String whitePath : pathWhiteList) {
            if (requestString.startsWith("/" + whitePath)) {
                //Deliver the requested file
                serveFile(httpExchange, new File(rootPath + requestString));
                return;
            }
        }

        //No other (allowed) file requested, therefore deliver the index (main) HTML file
        serveFile(httpExchange, indexFile);
    }
}
