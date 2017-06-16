package smart_things.coffee.server;

import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.FindEventListenersCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import smart_things.coffee.api.exceptions.AlreadyBrewingException;
import smart_things.coffee.api.exceptions.LowWaterException;
import smart_things.coffee.api.exceptions.NoCarafeException;
import smart_things.coffee.api.exceptions.NoWaterException;
import smart_things.coffee.api.main.SmartCoffeeMachine;
import smart_things.coffee.api.messages.StatusActivityTypes;
import smart_things.coffee.api.messages.StrengthCodes;
import smart_things.coffee.api.messages.WaterLevelCodes;
import smart_things.coffee.api.model.CoffeeMachine;
import smart_things.coffee.schema.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Objects from this class are able to establish a connection to the KAA server as an CoffeeServer endpoint
 * and to establish a connection to a coffee machine using the SmartCoffee API. Events that are received from KAA
 * will result in operations performed on the coffee machine and status changes on the coffee machine will result
 * in KAA events that are sent to all listening endpoints. The KAA user name and the matching access token and the host
 * and port of the coffee machine are specified within the object constructor.
 *
 * @author Jan
 */
public class CoffeeServer {

    //Static hashmaps that map coffee machine enum types from the SmartCoffee API to the equivalent KAA enum types
    private static HashMap<StatusActivityTypes, StatusTypes> statusMapping = new HashMap<>();
    private static HashMap<WaterLevelCodes, WaterLevelTypes> waterLevelMapping = new HashMap<>();
    private static HashMap<StrengthCodes, StrengthTypes> brewingStrengthMapping = new HashMap<>();

    //In this block the static hashmaps for the enumeration types are filled with data so that mappings can be
    // performed in the following code
    static {
        //StatusActivityTypes (API) --> StatusTypes (KAA)
        statusMapping.put(StatusActivityTypes.BREWING, StatusTypes.BREWING);
        statusMapping.put(StatusActivityTypes.DONE, StatusTypes.READY);
        statusMapping.put(StatusActivityTypes.GRINDING, StatusTypes.GRINDING);
        statusMapping.put(StatusActivityTypes.READY, StatusTypes.READY);
        statusMapping.put(StatusActivityTypes.UNKOWN, StatusTypes.UNKNOWN);

        //WaterLevelCodes (API) --> WaterLevelTypes (KAA)
        waterLevelMapping.put(WaterLevelCodes.EMPTY, WaterLevelTypes.EMPTY);
        waterLevelMapping.put(WaterLevelCodes.LESS, WaterLevelTypes.LESS);
        waterLevelMapping.put(WaterLevelCodes.LESS2, WaterLevelTypes.LESS);
        waterLevelMapping.put(WaterLevelCodes.HALF, WaterLevelTypes.HALF);
        waterLevelMapping.put(WaterLevelCodes.FULL, WaterLevelTypes.FULL);
        waterLevelMapping.put(WaterLevelCodes.UNKNOWN, WaterLevelTypes.UNKNOWN);

        //StrengthCodes (API) --> StrengthTypes (KAA)
        brewingStrengthMapping.put(StrengthCodes.WEAK, StrengthTypes.WEAK);
        brewingStrengthMapping.put(StrengthCodes.MEDIUM, StrengthTypes.MEDIUM);
        brewingStrengthMapping.put(StrengthCodes.STRONG, StrengthTypes.STRONG);
        brewingStrengthMapping.put(StrengthCodes.UNKNOWN, StrengthTypes.UNKNOWN);
    }

    //KAA user settings
    private String userId = null;
    private String userToken = null;
    //Coffee machine connection settings
    private String coffeeHost = null;
    private int coffeePort = -1;
    //Coffee machine attributes
    private SmartCoffeeMachine machine = null;
    private boolean isBrewing = false;
    //KAA attributes
    private KaaClient kaaClient = null;
    private volatile CoffeeEventClassFamily tecf = null;

    /**
     * Creates a new CoffeeServer instance with a specific connection configuration
     *
     * @param userId     KAA user ID. Must be the same for each endpoint to make a connection possible!
     * @param userToken  KAA user token ID. Must be the same for each endpoint to make a connection possible!
     * @param coffeeHost The host address of the coffee machine
     * @param coffeePort The port of the coffee machine to connect to
     */
    public CoffeeServer(String userId, String userToken, String coffeeHost, int coffeePort) {
        this.userId = userId;
        this.userToken = userToken;
        this.coffeeHost = coffeeHost;
        this.coffeePort = coffeePort;
    }

    /**
     * Initializes the KAA connection including necessary operations like the
     * attachment of the user and the attachment of listeners. After the connection is established and
     * all operations are performed, a callback is called
     *
     * @param callback The Runnable to call after the connection to KAA is established
     */
    public void initKaa(Runnable callback) {
        //Create a new KAA client with default start and stop methods
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            /**
             * Called after the KAA client is started.
             */
            @Override
            public void onStarted() {
            }

            /**
             * Called after the KAA client stopped.
             */
            @Override
            public void onStopped() {
            }
        });
        //Start the KAA client
        kaaClient.start();

        //Attaches the user with the former specified user data to KAA
        kaaClient.attachUser(userId, userToken, response -> {

            //Check for success
            if (response.getResult() == SyncResponseResultType.SUCCESS) {
                //Attachment was successful, call method to perform further KAA configurations
                onUserAttached(callback);
            } else {
                //Attachment was not successful, stop the KAA client
                kaaClient.stop();
                System.err.println("ERROR: Could not attach user to KAA.");
            }
        });
    }

    /**
     * Initializes the connection to the coffee machine using the former given connection configuration.
     *
     * @throws IOException Thrown when the connection to the coffee machine is not possible
     */
    public void initCoffeeMachine() {
        //Check whether the KAA coffee machine event class family is already instanciated
        if (tecf == null) {
            throw new IllegalStateException("KAA event class family is not initialized!");
        }

        //Create a new SmartCoffeeMachine object from the SmartCoffee API
        machine = new SmartCoffeeMachine(coffeeHost, coffeePort, new Runnable() {
            @Override
            public void run() {
                System.err.println("Lost connection during runtime.");
                //Try to reconnect to the coffee machine
                connectCoffeeMachine();
            }
        });

        //Try to connect to coffee machine
        connectCoffeeMachine();
    }

    /**
     * Performs the actual connection try. Tries to connect as long as a connection can be established.
     */
    private void connectCoffeeMachine() {
        //Check whether the KAA coffee machine event class family is already instanciated
        if (tecf == null) {
            throw new IllegalStateException("KAA event class family is not initialized!");
        }
        //Check if machine instance is null
        if(machine == null){
            throw new IllegalStateException("Coffee machine instance is not initialized");
        }

        //Check if the coffee machine is already connected
        if (machine.isConnected()) {
            //Already connected, try to disconnect for reconnect
            try {
                machine.disconnect();
                //Inform endpoints about disconnect
                tecf.sendEventToAll(new DisconnectEvent());
            } catch (IOException e) {
                //Not really a problem, because reconnect is ongoing
                System.err.println("Could not disconnect (again) from coffee machine. Possibly the connection is" +
                        "not alive.");
            }
        }

        //Invalidate local coffee machine brewing variable
        isBrewing = false;

        //Try to connect to the real coffee machine in a loop until a connection was established
        while (true) {
            try {
                System.out.println("Trying to connect to the coffee machine...");
                //Connect and define reconnect as connection error handle option
                machine.connect();
                //Connection is established, exit the reconnect loop
                break;
            } catch (IOException e) {
                //Connection is not possible, notify the other endpoints and propagate exception
                tecf.sendEventToAll(new TechnicalErrorEvent("Could not connect to coffee machine."));
                System.err.println("Could not connect to coffee machine. Retry in 10 seconds... .");
                //Wait 10 seconds
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e1) {
                    //Completley unexpected interruption exception
                    System.err.println("InterruptedException: " + e1.getMessage());
                }
            }
        }

        //Connection is established, notify the other endpoints
        System.out.println("Connection to coffee machine was established successfully.");
        tecf.sendEventToAll(new ConnectEvent());

        /*
        Add an observer to the SmartCoffeeMachine object that notifies the other endpoints about status changes
        of the coffee machine
        */
        machine.addPollingObserver((obj, arg) -> onCoffeeMachineUpdate((CoffeeMachine) arg));
    }

    /**
     * Notifies all listening endpoints about the current status data of a given coffee machine. Checks also if brewing
     * was finished since the last status update and sends a BrewingFinishedEvent in this case
     *
     * @param model The model of the coffee machine whose status should be sent
     */
    private void onCoffeeMachineUpdate(CoffeeMachine model) {
        //Is the coffee machine currently brewing?
        if (model.getStatus().equals(StatusActivityTypes.BREWING) ||
                model.getStatus().equals(StatusActivityTypes.GRINDING)) {
            isBrewing = true;
        } else {
            //Not brewing, check last brewing status
            if (isBrewing) {
                //Last time when the method was called the coffee machine was brewing, so it stopped brewing until now
                //and the coffee seems to be ready --> Notify the listening endpoints with an BrewingFinishedEvent
                tecf.sendEventToAll(new BrewingFinishedEvent());
            }
            //Refresh brewing status remember variable
            isBrewing = false;
        }
        //Notify the listening endpoints about the current status of the coffee machine
        sendInfoObject(model);
    }

    /**
     * Sends an info object containing all status information of a given coffee machine model to all listening
     * endpoints.
     *
     * @param model The model of the coffee machine whose status should be sent
     */
    private void sendInfoObject(CoffeeMachine model) {
        //Check whether the KAA coffee machine event class family is already instanciated
        if (tecf == null) {
            throw new IllegalStateException("KAA is not initialized!");
        }

        //Fetch the coffee machine's status, waterLevel and configured brewing strength from the model
        StatusTypes status = statusMapping.get(model.getStatus());
        WaterLevelTypes waterLevel = waterLevelMapping.get(model.getWaterLevel());
        StrengthTypes strength = brewingStrengthMapping.get(model.getBrewingStrength());

        //Create an info object from the coffee machine's data that can be sent to the other endpoints
        InfoObject infoObject = new InfoObject(status, model.isCarafe(), model.isFilter(), waterLevel, strength,
                model.getNumberOfCups());

        //Notify endpoints through an InfoResponseEvent
        tecf.sendEventToAll(new InfoResponseEvent(infoObject));
    }

    /**
     * Attaches a set of listeners to KAA that define the behaviour of the coffee server on incoming events.
     * After the listeners are attached, a callback is called
     *
     * @param callback The Runnable to call after the listeners are attached
     */
    private void onUserAttached(Runnable callback) {
        //Check whether the KAA client is already instantiated
        if (kaaClient == null) {
            throw new IllegalStateException("KAA client connection is not initialized!");
        }

        //Get the coffee machine's event class family from a family factory
        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        tecf = eventFamilyFactory.getCoffeeEventClassFamily();

        //Add listeners to the event class family
        tecf.addListener(new CoffeeEventClassFamily.Listener() {

            /**
             * Listener for receiving InfoRequest events. Replies by sending an InfoObject
             * in an InfoResponseEvent to all listening endpoints
             * @param event The received event. Does not contain any relevant attributes
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(InfoRequestEvent event, String source) {
                //Send info object to all listening endpoints
                sendInfoObject(machine.getCoffeeMachineModel());
            }

            /**
             * Listener for receiving SetCups events. Sets the number of cups on the coffee machine or fails by
             * sending a TechnicalError event to all listening endpoints
             * @param event The received event. Contains the number of cups to set as an attribute
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(SetCupsEvent event, String source) {
                //Try to set the number of cups on the coffee machine
                try {
                    machine.setCupsNumber(event.getNumberofCups());
                } catch (IOException e) {
                    //Connection error, send TechnicalError event
                    System.err.println("ERROR: Coffee machine is not reachable.");
                    tecf.sendEventToAll(new TechnicalErrorEvent("Coffee machine is not reachable!"));
                    //Try to reconnect to coffee machine
                    connectCoffeeMachine();
                }
            }

            /**
             * Listener for receiving SetStrength events. Sets the brewing strength on the coffee machine or fails by
             * sending a TechnicalError event to all listening endpoints
             * @param event The received event. Contains the brewing strength (StrengthTypes) to set as an attribute
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(SetStrengthEvent event, String source) {

                //Iterate over the brewingStrengthMapping to find the matching StrengthCode to this StrengthType
                StrengthCodes code = null;
                for (Map.Entry<StrengthCodes, StrengthTypes> e : brewingStrengthMapping.entrySet()) {
                    //Compare the API's StrengthCode with the KAA's StrengthType
                    if (e.getValue().equals(event.getStrenght())) {
                        //StrengthCode found, save it
                        code = e.getKey();
                        break;
                    }
                }

                //Throw an exception in the case of that the matching StrengthCode was not found
                if (code == null) {
                    throw new IllegalArgumentException("StrengthType " + event.getStrenght()
                            + " could not be found in the brewingStrengthMapping. "
                            + "No StrenghtCode exists for this StrengthType!");
                }

                //Try to set the brewing strength on the coffee machine
                try {
                    //Set brewing strength at the coffee machine
                    machine.setBrewingStrength(code);
                } catch (IOException e) {
                    //Connection error, send TechnicalError event
                    System.err.println("ERROR: Coffee machine is not reachable.");
                    tecf.sendEventToAll(new TechnicalErrorEvent("Coffee machine is not reachable!"));
                    //Try to reconnect to coffee machine
                    connectCoffeeMachine();
                }
            }

            /**
             * Listener for receiving SetHotplateTime events. Sets the hotplate time on the coffee machine or fails by
             * sending a TechnicalError event to all listening endpoints
             * @param event The received event. Contains the hotplate time to set as an attribute
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(SetHotplateTimeEvent event, String source) {
                //Try to set the hotplate time on the coffee machine
                try {
                    machine.setHotPlateTime(event.getTime());
                } catch (IOException e) {
                    //Connection error, send TechnicalError event
                    System.err.println("ERROR: Coffee machine is not reachable.");
                    tecf.sendEventToAll(new TechnicalErrorEvent("Coffee machine is not reachable!"));
                    //Try to reconnect to coffee machine
                    connectCoffeeMachine();
                }
            }

            /**
             * Listener for receiving ToggleBrewingType events. Toggles the brewing type (grinder or filter) on
             * the coffee machine or fails by sending a TechnicalError event to all listening endpoints
             * @param event The received event. Does not contain any relevant attributes
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(ToggleBrewingTypeEvent event, String source) {
                //Try to toggle the brewing type on the coffee machine
                try {
                    machine.changeBrewingType();
                } catch (IOException e) {
                    //Connection error, send TechnicalError event
                    System.err.println("ERROR: Coffee machine is not reachable.");
                    tecf.sendEventToAll(new TechnicalErrorEvent("Coffee machine is not reachable!"));
                    //Try to reconnect to coffee machine
                    connectCoffeeMachine();
                }
            }

            /**
             * Listener for receiving StopBrewing events. Stops the coffee machine's brewing process (if running)
             * or fails by sending a TechnicalError event to all listening endpoints
             * @param event The received event. Does not contain any relevant attributes
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(StopBrewingEvent event, String source) {
                //Try to stop the coffee machine's brewing process
                try {
                    machine.stopBrewing();
                } catch (IOException e) {
                    //Connection error, send TechnicalError event
                    System.err.println("ERROR: Coffee machine is not reachable.");
                    tecf.sendEventToAll(new TechnicalErrorEvent("Coffee machine is not reachable!"));
                    //Try to reconnect to coffee machine
                    connectCoffeeMachine();
                }
            }

            /**
             * Listener for receiving StartBrewing events. Requests the coffee machine to start brewing coffee
             * or fails by sending an exception event (AlreadyBrewing, NoCarafe, NoWater, LowWater) or a
             * TechnicalError event to all listening endpoints
             * @param event The received event. Does not contain any relevant attributes
             * @param source The source endpoint that has sent the event
             */
            @Override
            public void onEvent(StartBrewingEvent event, String source) {
                //Try to request the coffee machine to start brewing coffee
                try {
                    machine.brewCoffee();
                } catch (AlreadyBrewingException e) {
                    //Coffee machine is already brewing
                    tecf.sendEventToAll(new AlreadyBrewingEvent());
                } catch (NoCarafeException e) {
                    //Coffee machine does not recognize the carafe
                    tecf.sendEventToAll(new NoCarafeEvent());
                } catch (NoWaterException e) {
                    //Coffee machine has not enough water
                    tecf.sendEventToAll(new NoWaterEvent());
                } catch (LowWaterException e) {
                    //Coffee machine has less water for this number of cups but starts brewing
                    tecf.sendEventToAll(new LowWaterEvent());
                } catch (IOException e) {
                    //Connection error, send TechnicalError event
                    System.err.println("ERROR: Coffee machine is not reachable.");
                    tecf.sendEventToAll(new TechnicalErrorEvent("Coffee machine is not reachable!"));
                    //Try to reconnect to coffee machine
                    connectCoffeeMachine();
                }
            }
        });
        //Find all active event listeners of other endpoints
        findListeners();
        //Run callback Runnable
        callback.run();
    }

    /**
     * Finds all active event listeners of other running KAA endpoints so that events can be sent to these
     * endpoints afterwards.
     */
    private void findListeners() {
        //Check whether the KAA client is already instanciated
        if (kaaClient == null) {
            throw new IllegalStateException("KAA client connection is not initialized!");
        }

        //Filter: Only the listeners that receive events which are specified in this list will be found
        List<String> listenerFqns = new LinkedList<>();

        // Find all the listeners listening to events which are element of the FQNs list
        kaaClient.findEventListeners(listenerFqns, new FindEventListenersCallback() {

            /**
             * Called when the event listeners are received. Gives to possibility to perform any necessary actions
             * with the obtained event listeners
             * @param eventListeners The received event listerns
             */
            @Override
            public void onEventListenersReceived(List<String> eventListeners) {
                System.out.println(eventListeners.size() + " event listeners received");
            }

            /**
             * Called when the search request failed. Gives the possibility to perform any necessary actions
             * in case of failure.
             */
            @Override
            public void onRequestFailed() {
                System.err.println("Request failed");
            }
        });
    }

    /**
     * Stops the KAA server and disconnects from the coffee machine. Invalidates the local attributes.
     *
     * @throws IOException In case of a connection error to the coffee machine
     */
    public void stop() throws IOException {
        //Disconnect coffee machine and reset local variables as far as possible
        resetCoffeeMachine();

        //Is the KAA client instantiated?
        if (kaaClient != null) {
            //Stop KAA server
            kaaClient.stop();
        }


        //Invalidate local variables
        kaaClient = null;
        tecf = null;
    }

    /**
     * Disconnects from the current coffee machine (as far as it is connected at the moment) and rests the local
     * coffee machine variables.
     */
    private void resetCoffeeMachine() {
        //Is the coffee machine instantiated?
        if (machine != null) {
            //Disconnect coffee machine
            try {
                machine.disconnect();
            } catch (IOException e) {
                System.err.println("Could not disconnect (again) from coffee machine: " + e.getMessage());
            }

            //Inform other endpoints
            tecf.sendEventToAll(new DisconnectEvent());
        }

        //Invalidate local variables
        machine = null;
        isBrewing = false;
    }
}