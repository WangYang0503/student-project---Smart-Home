package smart_things.backend.server.main;

import smart_things.backend.server.action.ActionImplementation;
import smart_things.backend.server.action.implementation.Implementation1;
import smart_things.backend.server.database.DBManager;
import smart_things.backend.server.kaa.*;
import smart_things.backend.server.rules.RuleWorker;
import smart_things.backend.server.web.Server;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class of the backend server that brings the different components together. In here a KaaManager is initialized
 * that handles the connection to the Kaa middleware server, the event listeners and their event handlers are created
 * and the worker gets initialized to react on the received events.
 * <p>
 * Created by Jan on 10.01.2017.
 */
public class Main {

    //User specified concrete implementation of action methods. The name of the methods specified in the
    //implementation can be stored as "action method" in the rules ("then"-part)
    private static final ActionImplementation myActionImplementation = new Implementation1();

    //Port on which the admin web ui will be available
    private static final int webServerPort = 7777;

    //KAA user settings - must be the same for each KAA endpoint to make a communication possible!
    private static final String USER_EXTERNAL_ID = "coffee_user";
    private static final String USER_ACCESS_TOKEN = "coffee_user_token";

    //Kaa KaaManager that handles incoming events
    private KaaManager kaaManager = null;

    //KAA event listeners (summed up in a collction)
    private ListenerCollection listenerCollection = null;

    //RuleWorker that checks the specified rules
    private RuleWorker ruleWorker = null;

    //Database kaaManager that is able to communicate with the SQLite database in which the rules are stored
    private DBManager dbManager = null;

    //The server which serves the web admin ui
    private Server webServer = null;

    public Main() {
        //Try to create the database kaaManager
        try {
            dbManager = new DBManager();
        } catch (SQLException e) {
            MultiLogger.severe("SQLException while trying to create database: " + e.getMessage());
            System.exit(1);
        }

        //Create a new rule checker and pass database manager and the concrete action implementation later
        MultiLogger.infoNotify("Starting rule worker...");
        ruleWorker = new RuleWorker();

        //Setup kaa event listeners
        listenerCollection = new ListenerCollection(new CoffeeEventHandler(ruleWorker), new CarEventHandler(ruleWorker),
                new LightEventHandler(ruleWorker), new DoorEventHandler(ruleWorker), new SmokeEventHandler(ruleWorker));

        //Create new kaa kaaManager that uses the event listeners
        kaaManager = new KaaManager(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, listenerCollection);

        //Start the KAA server and wait for callback to setup the rule worker
        MultiLogger.infoNotify("Opening connection to KAA server...");
        kaaManager.initKaa(() -> {
            MultiLogger.infoNotify("Connection to KAA server is now alive");

            //Pass the Kaa server's ECFCollection to the own action implementation
            myActionImplementation.setEcfCollection(kaaManager.getEcfCollection());

            //Finally pass database manager and action implementation to the rule worker
            ruleWorker.setDBManager(dbManager);
            ruleWorker.setActionImplementation(myActionImplementation);
        });

        //Create a new http server to serve the web admin ui for rule administration and start it
        try {
            webServer = new Server(webServerPort, dbManager, ruleWorker);
            webServer.start();
            MultiLogger.infoNotify("Web server is now running and available on port " + webServerPort);
        } catch (IOException e) {
            MultiLogger.severeNotify("Error while creating the admin ui web server: " + e.getMessage());
        }

        //Add a shutdown hook so that the kaa client and the database connection are terminated correctly
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                MultiLogger.infoNotify("Stopping backend server...");
                //Stop Kaa endpoint client
                kaaManager.stop();
                //Shut down the database connection
                try {
                    dbManager.close();
                } catch (SQLException e) {
                    MultiLogger.severe("SQLException: " + e.getMessage());
                }
                //Stop the admin ui web server
                webServer.stop();
            }
        });
    }

    //application entry point
    public static void main(String args[]) {
        new Main();
    }
}