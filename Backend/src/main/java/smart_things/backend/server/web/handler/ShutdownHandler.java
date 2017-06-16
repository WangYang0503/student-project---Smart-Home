package smart_things.backend.server.web.handler;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
import com.sun.net.httpserver.HttpExchange;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.main.MultiLogger;
import smart_things.backend.server.rules.RuleWorker;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * This implementation of a http request handler is called when there are incoming "/shutdown" requests with which the
 * user wants to shutdown the server. Uses the AbstractHandler that implements some basic methods for handling http
 * requests as framework superclass.
 * <p>
 * Created by Jan on 20.01.2017.
 */
public class ShutdownHandler extends AbstractHandler {
    // Login data to bananapi server
    private static final String BANANA_IP = "172.24.1.10";
    private static final int BANANA_PORT = 22;
    private static final String BANANA_USER = "root";
    private static final String BANANA_PW = "bananapi";
    private static final String BANANA_COMMAND = "shutdown now";
    // Login data to raspi
    private static final String RASPI_IP = "172.24.1.1";
    private static final int RASPI_PORT = 22;
    private static final String RASPI_USER = "root";
    private static final String RASPI_PW = "raspi";
    private static final String RASPI_COMMAND = "shutdown now";

    /**
     * Creates a new http request handler using a given database manager to work with the database and a rule worker
     * to operate on the rules directly.
     *
     * @param dbManager  The database manager that has access to the database
     * @param ruleWorker The rule worker that is able to operate on the rules directly
     */
    public ShutdownHandler(DBManager dbManager, RuleWorker ruleWorker) {
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
        //TODO FOR MARIO
        //Do whatever you want to do

        System.out.println("**************SHUTTING DOWN***************");
        MultiLogger.infoNotify("The server will be shut down.");
        try {
            initSSHConnection();
            // Everything okay, send a success response
            sendSuccess(httpExchange);
        } catch (UnknownHostException e) {
            sendError(httpExchange, "UnknownHostException");
        } catch (IOException e) {
            // Nothing okay, send an error response
            sendError(httpExchange, "Could not connect to server via SSH");
        }
    }

    /**
     * Inits a SSH-Connection to the Bananapi-Server and executes a restart
     * command
     *
     * @throws IOException
     */
    private void initSSHConnection() throws IOException, UnknownHostException {
        String banana_result = new Shell.Plain(
                new SSHByPassword(BANANA_IP, BANANA_PORT, BANANA_USER, BANANA_PW)).exec(BANANA_COMMAND);
        MultiLogger.infoNotify("banana: " + banana_result);
        String raspi_result = new Shell.Plain(
                new SSHByPassword(RASPI_IP, RASPI_PORT, RASPI_USER, RASPI_PW)).exec(RASPI_COMMAND);
        MultiLogger.infoNotify("raspi: " + raspi_result);
    }
}
