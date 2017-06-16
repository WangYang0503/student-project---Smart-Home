package smart_things.backend.server.web;

import com.sun.net.httpserver.HttpServer;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.rules.RuleWorker;
import smart_things.backend.server.web.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Basic http web server that offers a administration web ui on a given port. The web ui can be accessed via the
 * browser using the IP address of the executing machine. The server uses different handlers to process the incoming
 * requests and a ThreadedPoolExecutor for multi-threaded handling of the requests.
 * <p>
 * Created by Jan on 18.01.2017.
 */
public class Server {

    //The port to use for the server
    private int port;
    //The http server that is used
    private HttpServer server = null;

    //Settings for the threaded pool executor that executes the site calls
    //Number of parallel running threads (Executor) on System
    private int corePoolSize = 2;
    //Maximum number of threads allowed in the pool
    private int maxPoolSize = 4;
    //Keep alive time for waiting threads for jobs (Runnable) in seconds
    private long keepAliveTime = 10;
    //Capacity of the array blocking queue
    private int queueCapacity = 10;

    /**
     * Creates a new HttpServer, using a ThreadPoolExecutor on a given port that allows the rule management
     * through a web ui.
     *
     * @param port       The port on which the HttpServer will be reachable
     * @param dbManager  The database manager that can be used to communicate with the database
     * @param ruleWorker The rule worker that can be used to operate directly on the rules
     * @throws IOException In case of and unexpected stream or I/O problem
     */
    public Server(int port, DBManager dbManager, RuleWorker ruleWorker) throws IOException {
        this.port = port;

        //Create new HttpServer on the defined port
        server = HttpServer.create(new InetSocketAddress(port), 0);

        //Add site locations as context with their handlers to the server and apply parameter filters on the contexts
        server.createContext("/", new MainHandler(dbManager, ruleWorker));
        server.createContext("/get", new GetRuleHandler(dbManager, ruleWorker));
        server.createContext("/add", new AddRuleHandler(dbManager, ruleWorker)).getFilters().add(new ParameterFilter());
        server.createContext("/edit", new EditRuleHandler(dbManager, ruleWorker)).getFilters().add(new ParameterFilter());
        server.createContext("/delete", new DeleteRuleHandler(dbManager, ruleWorker)).getFilters().add(new ParameterFilter());
        server.createContext("/switch", new SwitchRuleHandler(dbManager, ruleWorker)).getFilters().add(new ParameterFilter());
        server.createContext("/poll", new PollHandler(dbManager, ruleWorker)).getFilters().add(new ParameterFilter());
        server.createContext("/trigger", new TriggerHandler(dbManager, ruleWorker)).getFilters().add(new ParameterFilter());
        server.createContext("/restart", new RestartHandler(dbManager, ruleWorker));
        server.createContext("/shutdown", new ShutdownHandler(dbManager, ruleWorker));

        //Set a ThreadPoolExecutor as executor
        server.setExecutor(new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue(queueCapacity)));
    }

    /**
     * Starts the http server.
     */
    public void start() {
        server.start();
    }

    /**
     * Stops the http server.
     */
    public void stop() {
        server.stop(0);
    }
}
