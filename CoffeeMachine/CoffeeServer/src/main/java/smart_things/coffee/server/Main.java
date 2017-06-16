package smart_things.coffee.server;

/**
 * This class creates a CoffeeServer object and opens using this object a connection to KAA and to a coffee machine.
 * The KAA user name and access token and the coffee machine's port and host can be specified in the constants at
 * the head of this class.
 *
 * @author Jan
 */
public class Main {

    //KAA user settings - must be the same for each KAA endpoint to make a communication possible!
    private static final String USER_EXTERNAL_ID = "coffee_user";
    private static final String USER_ACCESS_TOKEN = "coffee_user_token";

    //Coffee machine connection settings
    private static final String HOST = "172.24.1.20";
    private static final int PORT = 2081;

    /**
     * Main method that creates one CoffeeServer and builds a connection to KAA and to the coffee machine with the in
     * the constants specified configuration.
     *
     * @param args Command line parameters (not used)
     */
    public static void main(String[] args) {
        //Create CoffeeServer
        CoffeeServer myServer = new CoffeeServer(USER_EXTERNAL_ID, USER_ACCESS_TOKEN, HOST, PORT);
        //Initialize the KAA connection and wait for the callback to initialize the coffee machine connection
        myServer.initKaa(() -> {
            //Initialize a connection to the coffee machine using the SmartCoffee API
            myServer.initCoffeeMachine();
        });
    }
}